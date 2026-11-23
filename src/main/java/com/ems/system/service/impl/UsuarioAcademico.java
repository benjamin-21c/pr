package com.ems.system.service.impl;

import com.ems.system.entity.Docente;
import com.ems.system.entity.Estudiante;
import lombok.AllArgsConstructor;
import lombok.Getter;

/** Identidad academica del usuario autenticado (public.sys_user -> persona -> docente/estudiante). */
@Getter
@AllArgsConstructor
public class UsuarioAcademico {

    private final Long userId;
    private final Docente docente;
    private final Estudiante estudiante;

    public boolean esDocente() {
        return docente != null;
    }

    public boolean esEstudiante() {
        return estudiante != null;
    }
}