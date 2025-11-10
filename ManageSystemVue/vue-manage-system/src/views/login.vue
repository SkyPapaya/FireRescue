<template>
  <div class="login-wrap">
    <div class="ms-login">
      <div class="ms-title">{{ isRegister ? '注册新用户' : '后台管理系统' }}</div>
      <el-form :model="param" :rules="rules" ref="loginFormRef" label-width="0px" class="ms-content">

        <el-form-item prop="username">
          <el-input v-model="param.username" placeholder="用户名">
            <template #prepend>
              <el-button :icon="User"></el-button>
            </template>
          </el-input>
        </el-form-item>

        <el-form-item prop="password">
          <el-input
              type="password"
              placeholder="密码"
              v-model="param.password"
              @keyup.enter="handleSubmit(loginFormRef)"
          >
            <template #prepend>
              <el-button :icon="Lock"></el-button>
            </template>
          </el-input>
        </el-form-item>

        <el-form-item v-if="isRegister" prop="confirmPassword">
          <el-input
              type="password"
              placeholder="确认密码"
              v-model="param.confirmPassword"
              @keyup.enter="handleSubmit(loginFormRef)"
          >
            <template #prepend>
              <el-button :icon="Lock"></el-button>
            </template>
          </el-input>
        </el-form-item>

        <div class="login-btn">
          <el-button type="primary" @click="handleSubmit(loginFormRef)">
            {{ isRegister ? '注册' : '登录' }}
          </el-button>
        </div>

        <div class="login-tips-right">
          <el-link type="primary" @click="toggleRegister">
            {{ isRegister ? '已有账户？去登录' : '没有账户？去注册' }}
          </el-link>
        </div>
      </el-form>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue';
import { useTagsStore } from '../store/tags';
// 1. 导入新的 userStore (已注释掉)
// import { useUserStore } from '../store/userStore';
import { useRouter } from 'vue-router';
import { ElMessage } from 'element-plus';
import type { FormInstance, FormRules } from 'element-plus';
import { Lock, User } from '@element-plus/icons-vue';
import service from '../utils/request';

// 登录/注册状态切换
const isRegister = ref(false);

const router = useRouter();
const param = reactive({
  username: '',
  password: '',
  confirmPassword: '',
});

// 确认密码的验证器
const validatePassConfirm = (rule: any, value: any, callback: any) => {
  if (value === '') {
    callback(new Error('请再次输入密码'));
  } else if (value !== param.password) {
    callback(new Error('两次输入密码不一致!'));
  } else {
    callback();
  }
};

const rules = reactive<FormRules>({
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, message: '密码长度不能少于6位', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, validator: validatePassConfirm, trigger: 'blur' }
  ],
});

const loginFormRef = ref<FormInstance>();
// 2. 获取 userStore 实例 (已注释掉)
// const userStore = useUserStore();

// 切换登录/注册模式
const toggleRegister = () => {
  isRegister.value = !isRegister.value;
  loginFormRef.value?.resetFields();
};

// 统一的提交处理
const handleSubmit = (formEl: FormInstance | undefined) => {
  if (!formEl) return;
  formEl.validate((valid: boolean) => {
    if (valid) {
      if (isRegister.value) {
        submitRegister(); // 调用注册
      } else {
        submitLogin(); // 调用登录
      }
    } else {
      ElMessage.error('表单验证失败');
      return false;
    }
  });
};

// 登录逻辑
const submitLogin = async () => {
  try {
    const res = await service.post('/user/login', {
      name: param.username,
      password: param.password
    });

    if (res.code === '200') {
      ElMessage.success('登录成功');
      const userData = res.data; // 后端返回的 UserDO

      // 3. 存储用户名 (用于 header 显示)
      localStorage.setItem('ms_username', userData.name);

      // 4. 【核心改动】调用 userStore action (已注释掉)
      // userStore.setAuthority(userData.authority || 'user');

      // 5. 跳转到首页 (此时路由守卫会介入)
      router.push('/');
    } else {
      ElMessage.error(res.message || '登录失败');
    }
  } catch (error) {
    console.error(error);
    ElMessage.error('登录请求失败，请检查后端服务。');
  }
};

// 注册逻辑 (保持不变)
const submitRegister = async () => {
  try {
    const res = await service.post('/user/register', {
      name: param.username,
      password: param.password
    });

    if (res.code === '200') {
      ElMessage.success('注册成功！请登录。');
      isRegister.value = false; // 自动切换回登录界面
      loginFormRef.value?.resetFields(); // 清空表单
    } else {
      ElMessage.error(res.message || '注册失败');
    }
  } catch (error) {
    console.error(error);
    ElMessage.error('注册请求失败，请检查后端服务是否运行。');
  }
};

const tags = useTagsStore();
tags.clearTags();
</script>

<style scoped>
/* (样式保持不变) */
.login-wrap {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 100%;
  height: 100%;
  background-image: url(../assets/img/login-bg.jpg);
  background-size: 100%;
}
.ms-title {
  line-height: 50px;
  text-align: center;
  font-size: 20px;
  color: #333;
  font-weight: bold;
  padding-top: 10px;
}
.ms-login {
  width: 350px;
  border-radius: 5px;
  background: #fff;
}
.ms-content {
  padding: 10px 30px 30px;
}
.login-btn {
  text-align: center;
}
.login-btn button {
  width: 100%;
  height: 36px;
  margin-bottom: 10px;
}
.login-tips-right {
  font-size: 12px;
  color: #333;
  text-align: right;
}
</style>