<template>
    <div> 
    <el-card>
        <h1>ABMFactura </h1> 
        <el-button type="primary" @click="abrirAdicionar"> 
        Adicionar 
        </el-button> 
        <el-table 
        :data="state.tableData" 
        border 
        row-key="id" 
        >
             <el-table-column 
             label="ID" 
             prop="id" 
             width="80" 
             align="center" 
             />
             <el-table-column
             label="Nombre"
             prop="nombre"
             />
             <el-table-column
             label="NIT"
             prop="nit"
             />
             <el-table-column
             label="Total"
             align="right"
             >
             <template #default="fila">
                {{ Number(fila.row.total || 0).toFixed(2)}}Bs.
             </template>
             </el-table-column>
             <el-table-column
             label="Acciones"
             width="220"
             align="center"
             >
             <template #default="fila">

             </template>
             </el-table-column>
             <el-table-column
             label="Acciones"
             width="220"
             align="center"
             >
             <template #default="fila">
                <el-button type="primary"
                @click="abrirModificar(fila.row)">
                    Modificar
                </el-button>
                <el-button
                type="danger"
                @click="eliminarFactura(fila.row)"
                >
                    Eliminar
                </el-button>
             </template>
             </el-table-column>
        </el-table>
    </el-card>
    <EditFactura
    ref="editFacturaRef"
    @get-list="listarFacturas"
    />
        <pagination v-model:current="state.current" v-model:size="state.size" v-model:total="state.total" @get-list="listarFacturas"></pagination>
    </div>
</template>
<script setup>
import { onMounted, reactive, ref } from 'vue';
import { ElMessageBox } from 'element-plus';
import Pagination from "@/components/Pagination.vue";
import EditFactura from './editFactura2.vue'
import{
    getMiFacturaList,
    delMiFactura
}from '../../api/mifactura/mifactura.js'
const state = reactive ({
    blurry: '',
    tableData: [],
    current: 1,
    size: 10,
    total: 0
})
const editFacturaRef = ref(null)
onMounted(()=>{
listarFacturas()
})
const listarFacturas =() => {
    const params= {
        blurry: state.blurry,
        size: state.size,
        currentPage: state.current,
    }

getMiFacturaList(params).then((res)=>{
    if (res.success) {
        state.tableData = res.data.records || []
        state.total = res.data.total
    }
})
}
const abrirAdicionar = () => {
    editFacturaRef.value.open()
}
abrirModificar = (row) => {
    editFacturaRef.value.open(row)
}
const eliminarFactura = (row) => {
    ElMessageBox.confirm(
        '¿Esta seguro de eliminar la factura' + row.id + '?',
        'Confirmar',
        {
            confirmarButtonText: 'Si',
            cancelButtonText:'No',
            type: 'warning'
        }
    ).then(() =>{
        delMiFactura({id: row.id}).then((res)=>{
            if (res.success){
                listarFacturas()
            } else{
                alert(res.msg)
            }
        })
    })
}
</script>