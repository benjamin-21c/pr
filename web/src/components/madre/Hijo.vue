<template>
  <el-dialog v-model="dialogVisible" title="Gestionar Hijos" width="900px" @close="onClose">
    <el-form :inline="true" class="hijo-form">
      <el-form-item label="CIH">
        <el-input v-model="nuevo.cih" placeholder="Carnet del hijo" style="width: 150px" />
      </el-form-item>
      <el-form-item label="CIM">
        <el-input v-model="nuevo.cim" disabled style="width: 150px" />
      </el-form-item>
      <el-form-item label="Nombre">
        <el-input v-model="nuevo.nombre" placeholder="Nombre" style="width: 160px" />
      </el-form-item>
      <el-form-item label="Apellido Paterno">
        <el-input v-model="nuevo.ap" placeholder="Apellido paterno" style="width: 160px" />
      </el-form-item>
      <el-form-item label="Apellido Materno">
        <el-input v-model="nuevo.am" placeholder="Apellido materno" style="width: 160px" />
      </el-form-item>
    </el-form>

    <el-button type="primary" :icon="Plus" class="hijo-agregar-btn" @click="agregarHijo">
      Agregar hijo
    </el-button>

    <el-table
      v-loading="loading"
      :data="hijos"
      border
      class="hijo-tabla"
      header-cell-class-name="hijo-tabla-header"
      empty-text="No hay hijos registrados"
    >
      <el-table-column type="index" label="Item" width="70" align="center" />
      <el-table-column prop="cih" label="CIH" width="140" align="center" />
      <el-table-column prop="nombre" label="Nombre" min-width="140" />
      <el-table-column prop="ap" label="Apellido Paterno" min-width="140" />
      <el-table-column prop="am" label="Apellido Materno" min-width="140" />
      <el-table-column label="Accion" width="110" align="center">
        <template #default="{ row }">
          <el-button type="danger" size="small" @click="quitarHijo(row)">Quitar</el-button>
        </template>
      </el-table-column>
    </el-table>

    <template #footer>
      <el-button @click="dialogVisible = false">Cancelar</el-button>
      <el-button type="primary" @click="dialogVisible = false">Guardar</el-button>
    </template>
  </el-dialog>
</template>

<script setup>
import { computed, reactive, ref, watch } from 'vue'
import { ElMessage } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'
import { getHijoList, editHijo, delHijo } from '@/api/hijo/hijo'

const props = defineProps({
  visible: { type: Boolean, default: false },
  madre: { type: Object, default: null }
})
const emit = defineEmits(['update:visible', 'cerrado'])

const dialogVisible = computed({
  get: () => props.visible,
  set: (val) => emit('update:visible', val)
})

const hijos = ref([])
const loading = ref(false)
const nuevo = reactive({ cih: '', cim: '', nombre: '', ap: '', am: '' })

watch(
  () => props.visible,
  (val) => {
    if (val && props.madre) {
      resetForm()
      cargarHijos()
    }
  }
)

function resetForm() {
  nuevo.cih = ''
  nuevo.cim = props.madre?.cim || ''
  nuevo.nombre = ''
  nuevo.ap = props.madre?.ap || ''
  nuevo.am = props.madre?.am || ''
}

async function cargarHijos() {
  loading.value = true
  try {
    const { data } = await getHijoList({ cim: props.madre.cim })
    hijos.value = data || []
  } catch (error) {
    ElMessage.error(error?.message || 'No se pudo cargar la lista de hijos')
  } finally {
    loading.value = false
  }
}

async function agregarHijo() {
  if (!nuevo.cih || !nuevo.nombre || !nuevo.ap || !nuevo.am) {
    ElMessage.warning('Todos los campos son obligatorios')
    return
  }
  try {
    await editHijo({ ...nuevo })
    ElMessage.success('Hijo agregado')
    await cargarHijos()
    nuevo.cih = ''
    nuevo.nombre = ''
    nuevo.ap = props.madre?.ap || ''
    nuevo.am = props.madre?.am || ''
  } catch (error) {
    ElMessage.error(error?.message || 'No se pudo agregar el hijo')
  }
}

async function quitarHijo(row) {
  try {
    await delHijo({ cih: row.cih })
    ElMessage.success('Hijo eliminado')
    await cargarHijos()
  } catch (error) {
    ElMessage.error(error?.message || 'No se pudo eliminar el hijo')
  }
}

function onClose() {
  emit('cerrado')
}
</script>

<style scoped>
.hijo-form {
  margin-bottom: 4px;
}

.hijo-agregar-btn {
  margin-bottom: 16px;
}

.hijo-tabla :deep(.hijo-tabla-header) {
  background-color: #f0f2f5 !important;
  color: #303133;
  font-weight: 600;
}
</style>
