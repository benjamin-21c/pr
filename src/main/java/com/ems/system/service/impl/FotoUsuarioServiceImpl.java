package com.ems.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ems.common.exception.BadRequestException;
import com.ems.system.entity.FotoUsuario;
import com.ems.system.mapper.FotoUsuarioMapper;
import com.ems.system.service.FotoUsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Guarda la fotografia en disco y sus metadatos en academico.foto_usuario (una foto por usuario).
 * Configuracion opcional en application.yml:
 *   academico.foto.directorio  (por defecto: uploads/fotos)
 *   academico.foto.rol-admin   (role_code del administrador en sys_role, por defecto: ADMIN)
 */
@Service
@RequiredArgsConstructor
public class FotoUsuarioServiceImpl extends ServiceImpl<FotoUsuarioMapper, FotoUsuario>
        implements FotoUsuarioService {

    private static final long MAX_BYTES = 2L * 1024 * 1024;

    private final JdbcTemplate jdbcTemplate;

    @Value("${academico.foto.directorio:uploads/fotos}")
    private String directorio;

    @Value("${academico.foto.rol-admin:ADMIN}")
    private String rolAdmin;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public FotoUsuario uploadFoto(Long userId, MultipartFile file, String username) {
        if (userId == null) {
            throw new BadRequestException("El usuario es obligatorio");
        }
        if (file == null || file.isEmpty()) {
            throw new BadRequestException("Debe adjuntar una imagen");
        }
        if (!esAdministrador(username)) {
            throw new BadRequestException("Solo el administrador puede cargar fotografias");
        }
        Integer existe = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM public.sys_user WHERE id = ?", Integer.class, userId);
        if (existe == null || existe == 0) {
            throw new BadRequestException("El usuario no existe");
        }
        if (file.getSize() > MAX_BYTES) {
            throw new BadRequestException("La imagen no puede superar 2 MB");
        }

        String mime = file.getContentType();
        String extension = extensionDe(mime);
        if (extension == null) {
            throw new BadRequestException("Solo se permiten imagenes JPEG, PNG o WebP");
        }

        Path carpeta = Paths.get(directorio).toAbsolutePath().normalize();
        // El nombre lo genera el sistema: nunca se usa el nombre que envia el cliente
        String nombreArchivo = UUID.randomUUID() + "." + extension;
        Path destino = carpeta.resolve(nombreArchivo).normalize();
        if (!destino.startsWith(carpeta)) {
            throw new BadRequestException("Nombre de archivo no valido");
        }

        try {
            if (!contenidoCoincide(file, mime)) {
                throw new BadRequestException("El contenido del archivo no corresponde a una imagen valida");
            }
            Files.createDirectories(carpeta);
            try (InputStream in = file.getInputStream()) {
                Files.copy(in, destino, StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException e) {
            throw new BadRequestException("No se pudo guardar la fotografia");
        }

        try {
            FotoUsuario actual = this.getOne(
                    new LambdaQueryWrapper<FotoUsuario>().eq(FotoUsuario::getUserId, userId), false);
            FotoUsuario foto = actual != null ? actual : new FotoUsuario();
            String anterior = actual != null ? actual.getRutaRelativa() : null;

            foto.setUserId(userId);
            foto.setNombreArchivo(nombreArchivo);
            foto.setRutaRelativa(nombreArchivo);
            foto.setMimeType(mime);
            foto.setTamanoBytes((int) file.getSize());
            foto.setFechaCarga(LocalDateTime.now());
            this.saveOrUpdate(foto);

            if (anterior != null) {
                try {
                    Files.deleteIfExists(carpeta.resolve(anterior).normalize());
                } catch (IOException ignored) {
                    // la foto nueva ya quedo registrada; un archivo viejo huerfano no es critico
                }
            }
            return foto;
        } catch (RuntimeException e) {
            // Si falla la base de datos no deja el archivo nuevo huerfano en disco
            try {
                Files.deleteIfExists(destino);
            } catch (IOException ignored) {
                // nada mas que hacer
            }
            throw e;
        }
    }

    private boolean esAdministrador(String username) {
        Integer n = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM public.sys_user u "
                        + "JOIN public.sys_role_user ru ON ru.user_id = u.id "
                        + "JOIN public.sys_role r ON r.id = ru.role_id "
                        + "WHERE u.username = ? AND r.role_code = ?",
                Integer.class, username, rolAdmin);
        return n != null && n > 0;
    }

    private String extensionDe(String mime) {
        if ("image/jpeg".equals(mime)) {
            return "jpg";
        }
        if ("image/png".equals(mime)) {
            return "png";
        }
        if ("image/webp".equals(mime)) {
            return "webp";
        }
        return null;
    }

    /** Verifica los primeros bytes (firma del formato), no solo el Content-Type que declara el cliente. */
    private boolean contenidoCoincide(MultipartFile file, String mime) throws IOException {
        byte[] h = new byte[12];
        int leidos;
        try (InputStream in = file.getInputStream()) {
            leidos = in.read(h);
        }
        if (leidos < 12) {
            return false;
        }
        if ("image/jpeg".equals(mime)) {
            return (h[0] & 0xFF) == 0xFF && (h[1] & 0xFF) == 0xD8 && (h[2] & 0xFF) == 0xFF;
        }
        if ("image/png".equals(mime)) {
            return (h[0] & 0xFF) == 0x89 && h[1] == 'P' && h[2] == 'N' && h[3] == 'G';
        }
        // WebP: "RIFF" .... "WEBP"
        return h[0] == 'R' && h[1] == 'I' && h[2] == 'F' && h[3] == 'F'
                && h[8] == 'W' && h[9] == 'E' && h[10] == 'B' && h[11] == 'P';
    }
}