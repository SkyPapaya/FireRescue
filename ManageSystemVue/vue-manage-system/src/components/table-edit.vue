<template>

  <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
    <el-form-item label="用户名" prop="name">
      <el-input v-model="form.name"></el-input>
    </el-form-item>

    <el-form-item label="性别" prop="sex">
      <el-input v-model="form.sex"></el-input>
    </el-form-item>

    <el-form-item label="ID" prop="id">
      <el-input v-model.number="form.id"></el-input>
    </el-form-item>

    <el-form-item label="邮箱" prop="e_mail">
      <el-input v-model.number="form.e_mail"></el-input>
    </el-form-item>

    <el-form-item label="电话号码" prop="phone">
      <el-input v-model="form.address"></el-input>
    </el-form-item>

    <el-form-item label="权限" prop="authority">
      <el-input v-model="form.authority"></el-input>
    </el-form-item>

    <el-form-item label="address" prop="authority">
      <el-input v-model="form.address"></el-input>
    </el-form-item>

    <el-form-item label="注册日期" prop="date">
      <el-date-picker type="date" v-model="form.date" value-format="YYYY-MM-DD"></el-date-picker>
    </el-form-item>
    <el-form-item>
      <el-button type="primary" @click="saveEdit(formRef)">保 存</el-button>
    </el-form-item>
  </el-form>
</template>

<script lang="ts" setup>
import {ElMessage, FormInstance, FormRules, UploadProps} from 'element-plus';
import {ref} from 'vue';

const props = defineProps({
  data: {
    type: Object,
    required: true
  },
  edit: {
    type: Boolean,
    required: false
  },
  update: {
    type: Function,
    required: true
  }
});

const defaultData = {
  id: 0,
  name: 'SkyPapaya',
  e_mail: '898496136@qq.cpm',
  phone: '15285615707',
  authority: 0,
  address: 'NJUPT',
  sex:'male',
  createdTime: new Date()
};

const form = ref({...(props.edit ? props.data : defaultData)});

const rules: FormRules = {
  name: [{required: true, message: '用户名', trigger: 'blur'}]
};
const formRef = ref<FormInstance>();
const saveEdit = (formEl: FormInstance | undefined) => {
  if (!formEl) return;
  formEl.validate(valid => {
    if (!valid) return false;
    props.update(form.value);
    ElMessage.success('保存成功！');
  });
};

const handleAvatarSuccess: UploadProps['onSuccess'] = (response, uploadFile) => {
  form.value.thumb = URL.createObjectURL(uploadFile.raw!);
};

const beforeAvatarUpload: UploadProps['beforeUpload'] = rawFile => {
  if (rawFile.type !== 'image/jpeg') {
    ElMessage.error('Avatar picture must be JPG format!');
    return false;
  } else if (rawFile.size / 1024 / 1024 > 2) {
    ElMessage.error('Avatar picture size can not exceed 2MB!');
    return false;
  }
  return true;
};
</script>

<style>
.avatar-uploader .el-upload {
  border: 1px dashed var(--el-border-color);
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  transition: var(--el-transition-duration-fast);
}

.avatar-uploader .el-upload:hover {
  border-color: var(--el-color-primary);
}

.el-icon.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 178px;
  height: 178px;
  text-align: center;
}
</style>
