<template>
    <div class="container">
        <PostDetalle
        v-for="ve in informacion"
        :key="ve.titulo"
        :titulo="ve.detalle"
        @enviarPadre="mostrarAlerta"
        ></PostDetalle>
    </div>
</template>
<script setup>
import PostDetalle from "../ciclo/PostDetalle.vue";
import { ref } from "vue";
let informacion = ref ([
    { titulo: "TITULO 1", detalle: "detalle del titulo 1 VD"},
    { titulo: "t.- 2", detalle: "detalle del titulo 2 VD"},
    { titulo: "t.- 3", detalle: "detalle del titulo 3"},
    { titulo: "t.- 4", detalle: "detalle del titulo 4"},
    { titulo: "t.-", detalle: "detalle del titulo 5"},
]);
function mostrarAlerta(cadena){
    alert(cadena);
}
</script>