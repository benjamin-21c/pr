<template>
    <h1>Codigo Compositions API</h1>
    <h2>{{contador}}</h2>
    <el-button @click="aumentar" type="primary">aumentar</el-button>
    <el-button @click="disminuir" type="danger">disminuir</el-button>
    <el-button @click="reiniciar">reiniciar</el-button>
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