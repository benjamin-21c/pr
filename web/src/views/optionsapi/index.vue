<template>
    <h1>Options API</h1>

    <h2>Carlos Salinas (Tapitas): {{ tapitas }}</h2>
    <el-button @click="votarTapitas" type="primary">Votar por Tapitas</el-button>
    <br>
    <br>

    <h2>Eduardo Cortez (Lalo): {{ lalo }}</h2>
    <el-button @click="votarLalo" type="success">Votar por Lalo</el-button>
    <br>
    <br>

    <el-button @click="reiniciar" type="danger">Reiniciar votos</el-button>
    <br>
    <br>

    <h2>{{ mensaje }}</h2>

</template>

<script>
export default {
    name: "EleccionesOptions",
    data() {
        return {
            tapitas: 0,
            lalo: 0
        }
    },

    methods: {
        votarTapitas() {
            this.tapitas++
        },

        votarLalo() {
            this.lalo++
        },

        reiniciar() {
            this.tapitas = 0
            this.lalo = 0
        }
    },

    computed: {
        mensaje() {
            if (this.tapitas > this.lalo) {
                return 'Va ganando Carlos Salinas (Tapitas)'
            } else if (this.lalo > this.tapitas) {
                return 'Va ganando Eduardo Cortez (Lalo)'
            } else {
                return 'Existe empate'
            }
        }
    }
}
</script>