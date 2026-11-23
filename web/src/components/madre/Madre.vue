<template>
  <div class="madre-container">
    <h1 class="madre-titulo">ABM Madre</h1>

    <div class="madre-toolbar">
      <el-button type="primary" :icon="Plus" @click="abrirAdicionar">Adicionar</el-button>
    </div>

    <el-table
      v-loading="loading"
      :data="madres"
      border
      class="madre-tabla"
      header-cell-class-name="madre-tabla-header"
      empty-text="No hay madres registradas"
    >
      <el-table-column prop="cim" label="CIM" width="160" align="center" />
      <el-table-column prop="nombre" label="Nombre" min-width="160" />
      <el-table-column prop="ap" label="Apellido Paterno" min-width="160" />
      <el-table-column prop="am" label="Apellido Materno" min-width="160" />
      <el-table-column label="Acciones" width="320" align="center">
        <template #default="{ row }">
          <el-button type="primary" :icon="Edit" size="small" @click="abrirModificar(row)">
            Modificar
          </el-button>
          <el-button type="success" :icon="User" size="small" @click="abrirHijos(row)">
            Hijos
          </el-button>
          <el-popconfirm
            title="¿Eliminar esta madre y sus hijos?"
            confirm-button-text="Si"
            cancel-button-text="No"
            @confirm="eliminarMadre(row)"
          >
            <template #reference>
              <el-button type="danger" :icon="Delete" size="small">Eliminar</el-button>
            </template>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog
      v-model="dialogVisible"
      :title="esEdicion ? 'Modificar Madre' : 'Adicionar Madre'"
      width="480px"
    >
      <el-form :model="form" label-position="top">
        <el-form-item label="CIM">
          <el-input v-model="form.cim" :disabled="esEdicion" placeholder="Carnet de identidad" />
        </el-form-item>
        <el-form-item label="Nombre">
          <el-input v-model="form.nombre" placeholder="Nombre" />
        </el-form-item>
        <el-form-item label="Apellido Paterno">
          <el-input v-model="form.ap" placeholder="Apellido paterno" />
        </el-form-item>
        <el-form-item label="Apellido Materno">
          <el-input v-model="form.am" placeholder="Apellido materno" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">Cancelar</el-button>
        <el-button type="primary" :loading="guardando" @click="guardarMadre">Guardar</el-button>
      </template>
    </el-dialog>

    <Hijo v-model:visible="hijosVisible" :madre="madreSeleccionada" @cerrado="cargarMadres" />
  </div>
</template>

<script setup>
import { onMounted, reactive, ref } from 'vue'
import { ElMessage } from 'element-plus'
import { Plus, Edit, User, Delete } from '@element-plus/icons-vue'
import { getMadreList, editMadre, delMadre } from '@/api/madre/madre'
import Hijo from './Hijo.vue'

const madres = ref([])
const loading = ref(false)

const dialogVisible = ref(false)
const esEdicion = ref(false)
const guardando = ref(false)
const form = reactive({ cim: '', nombre: '', ap: '', am: '' })

const hijosVisible = ref(false)
const madreSeleccionada = ref(null)

async function cargarMadres() {
  loading.value = true
  try {
    const { data } = await getMadreList()
    madres.value = data || []
  } catch (error) {
    ElMessage.error(error?.message || 'No se pudo cargar la lista de madres')
  } finally {
    loading.value = false
  }
}

function abrirAdicionar() {
  esEdicion.value = false
  form.cim = ''
  form.nombre = ''
  form.ap = ''
  form.am = ''
  dialogVisible.value = true
}

function abrirModificar(row) {
  esEdicion.value = true
  form.cim = row.cim
  form.nombre = row.nombre
  form.ap = row.ap
  form.am = row.am
  dialogVisible.value = true
}

async function guardarMadre() {
  if (!form.cim || !form.nombre || !form.ap || !form.am) {
    ElMessage.warning('Todos los campos son obligatorios')
    return
  }
  guardando.value = true
  try {
    await editMadre({ ...form })
    ElMessage.success('Operacion exitosa')
    dialogVisible.value = false
    await cargarMadres()
  } catch (error) {
    ElMessage.error(error?.message || 'No se pudo guardar la madre')
  } finally {
    guardando.value = false
  }
}

async function eliminarMadre(row) {
  try {
    await delMadre({ cim: row.cim })
    ElMessage.success('Eliminacion exitosa')
    await cargarMadres()
  } catch (error) {
    ElMessage.error(error?.message || 'No se pudo eliminar la madre')
  }
}

function abrirHijos(row) {
  madreSeleccionada.value = row
  hijosVisible.value = true
}

onMounted(cargarMadres)
</script>

<style scoped>
.madre-container {
  padding: 20px;
}

.madre-titulo {
  font-size: 30px;
  font-weight: 700;
  margin: 0 0 20px;
  color: #1f1f1f;
}

.madre-toolbar {
  margin-bottom: 16px;
}

.madre-tabla :deep(.madre-tabla-header) {
  background-color: #f0f2f5 !important;
  color: #303133;
  font-weight: 600;
}
</style>
