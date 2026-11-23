<template>
    <h1>Composition API</h1>

    <h2>Carlos Salinas (Tapitas): {{ tapitas }}</h2>
    <el-button @click="votarTapitas" type="primary">Votar por Tapitas</el-button>

    <br><br>

    <h2>Eduardo Cortez (Lalo): {{ lalo }}</h2>
    <el-button @click="votarLalo" type="success">Votar por Lalo</el-button>

    <br><br>

    <el-button @click="reiniciar" type="danger">Reiniciar votos</el-button>

    <br><br>

    <h2>{{ mensaje }}</h2>

</template>

<script setup>
import { ref, computed } from 'vue'

const tapitas = ref(0)
const lalo = ref(0)

const votarTapitas = () => {
    tapitas.value++
}

const votarLalo = () => {
    lalo.value++
}

const reiniciar = () => {
    tapitas.value = 0
    lalo.value = 0
}

const mensaje = computed(() => {
    if (tapitas.value > lalo.value) {
        return 'Va ganando Carlos Salinas (Tapitas)'
    } else if (lalo.value > tapitas.value) {
        return 'Va ganando Eduardo Cortez (Lalo)'
    } else {
        return 'Existe empate'
    }
})
</script>