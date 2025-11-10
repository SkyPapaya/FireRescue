<template>
  <el-descriptions title="" :column="2" border>
    <el-descriptions-item>
      <template #label> 用户ID </template>
      {{ data.id }}
    </el-descriptions-item>
    <el-descriptions-item>
      <template #label> 用户名 </template>
      {{ data.name }}
    </el-descriptions-item>
    <el-descriptions-item>
      <template #label> 邮箱 </template>
      {{ data.e_mail || '未提供' }}
    </el-descriptions-item>
    <el-descriptions-item>
      <template #label> 电话号码 </template>
      {{ data.phone || '未提供' }}
    </el-descriptions-item>
    <el-descriptions-item :span="2">
      <template #label> 权限 </template>
      {{ data.authority || 'N/A' }}
    </el-descriptions-item>
    <el-descriptions-item>
      <template #label> 地址 </template>
      {{ data.address || '未提供' }}
    </el-descriptions-item>
    <el-descriptions-item>
      <template #label> 注册时间 </template>
      {{ data.gmtCreated || 'N/A' }}
    </el-descriptions-item>
  </el-descriptions>
</template>

<script lang="ts" setup>
const props = defineProps({
  data: {
    type: Object,
    required: true
  }
});
</script>