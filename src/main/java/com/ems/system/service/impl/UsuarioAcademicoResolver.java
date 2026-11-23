package com.ems.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ems.common.exception.BadRequestException;
import com.ems.system.entity.Docente;
import com.ems.system.entity.Estudiante;
import com.ems.system.entity.Persona;
import com.ems.system.entity.SysUser;
import com.ems.system.mapper.DocenteMapper;
import com.ems.system.mapper.EstudianteMapper;
import com.ems.system.mapper.PersonaMapper;
import com.ems.system.mapper.SysUserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Convierte el username del JWT en la identidad academica del usuario.
 * Un usuario sin persona docente/estudiante (administracion, direccion) devuelve ambos en null.
 */
@Component
@RequiredArgsConstructor
public class UsuarioAcademicoResolver {

    private final SysUserMapper sysUserMapper;
    private final PersonaMapper personaMapper;
    private final DocenteMapper docenteMapper;
    private final EstudianteMapper estudianteMapper;

    public UsuarioAcademico resolver(String username) {
        SysUser user = sysUserMapper.selectOne(
                new LambdaQueryWrapper<SysUser>().eq(SysUser::getUsername, username));
        if (user == null) {
            throw new BadRequestException("Usuario no encontrado");
        }
        Persona persona = personaMapper.selectOne(
                new LambdaQueryWrapper<Persona>().eq(Persona::getUserId, user.getId()));
        Docente docente = null;
        Estudiante estudiante = null;
        if (persona != null) {
            docente = docenteMapper.selectOne(
                    new LambdaQueryWrapper<Docente>().eq(Docente::getPersonaId, persona.getId()));
            estudiante = estudianteMapper.selectOne(
                    new LambdaQueryWrapper<Estudiante>().eq(Estudiante::getPersonaId, persona.getId()));
        }
        return new UsuarioAcademico(user.getId(), docente, estudiante);
    }
}