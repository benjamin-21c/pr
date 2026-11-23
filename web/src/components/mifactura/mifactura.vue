<template>
    <div>
        <el-card>
            <el-table
            :data="state.tableData"
            row-key="id"
            >
        <el-table-column
        label="Mis nombres"
        prop="nit"
        />
        <el-table-column
        label="Mis nombres"
        prop="nombre"
        />
        </el-table>
        </el-card>
    </div>
</template>
<script setup>
import { onMounted, reactive } from 'vue';
import { getMiFacturaList } from '../../api/mifactura/mifactura';

const state = reactive({
    tableData: []
})
onMounted(()=>{
    listarFacturas()
})

const listarFacturas = () => {
    const params ={
        currentPage: 1,
        size: 100
    }
    getMiFacturaList(params).then((res) =>{
        if(res.success){
                state.tableData = res.data.records || []
                console.log(state.tableData)
        }
    })
}
</script>