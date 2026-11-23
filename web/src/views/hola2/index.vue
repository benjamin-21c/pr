<template>
    <h1>Codigo Compositions API</h1>
    <h2>{{contador}}</h2>
    <el-button type="primary" @click="aumentar" >aumentar</el-button>
    <el-button type="danger" @click="disminuir" >disminuir</el-button>
    <el-button type="warning" @click="reiniciar" >reiniciar</el-button>
</template>
<script setup>
import { ref } from 'vue';
const contador=ref(0)
const incrementar = () =>{
    contador.value++
}
const disminuir = () =>{
    contador.value--
}
const reiniciar = () =>{
    contador.value =0
}
</script>