<template>
<el-form :model="form" :rules="rules">
 <el-form-item label="Introduzca Nombre" prop="nombre" ref="formRef">
    <el-input v-model="form.nombre" placeHolder="sin nombre" />
 </el-form-item>

 <el-form-item label="Introduzca su Correo" prop="correo">
    <el-input v-model="form.correo" placeHolder="ejemplo@gmail.com" />
 </el-form-item>

 <el-form-item label="Introduzca su Edad">
    <el-input v-model="form.edad" placeHolder="su edad" />
</el-form-item>

<el-form-item>
    <el-button @click="enviarFormulario" v-model="form.aceptar" type="primary">Enviar</el-button>
</el-form-item>

</el-form>
</template>
<script setup>
import {reactive, ref} from 'vue'
import {ElMessage} from 'element-plus'

const formRef=ref(null)

const form = reactive({
    nombre: 'sin nombre',
    correo: 'sincorreo@gmail.com',
    edad: 0,
})

const enviarFormulario = async() =>{
    if(!formRef.value) return
    await
    formRef.value.validate((valid)=>{
        if(valid){
            ElMessage.success('Formulario Enviado Correctamente')
        }else{
            ElMessage.error('Revise los campos del formulario')
        }

    })
}
/*const enviarFormulario = ()=> {
    console.log (form)
}*/

const rules ={
    nombre: [{
        required: true,
        message: 'El nombre es un campo obligatorio',
        trigger: 'blur'
    },{
        min: 3,
        message:'El nombre debe tener almenos 3 caracteres',
        trigger: 'blur'
    },{
        validator: validarNombre,
        trigger: 'change'
    }],
    
    correo: [{
        type: 'email',
        message: 'Correo Invalido',
        trigger: ['blur', 'change']
    }]
}


const validarNombre = (rule, value, callback) => {
  if (value === "evo" || value === "lara") {
    callback(new Error("No ponga esos nombres"))
     ElMessage.success('No ponga esos nombres')
  } else {
    callback()
  }
}
</script>