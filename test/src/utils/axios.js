

import axios from 'axios';

// 设置全局默认配置
axios.defaults.baseURL = 'http://localhost:8080';  // 设置后端API的基础地址
axios.defaults.withCredentials = true;  // 配置跨域请求时携带凭证（cookies等）

export default axios;
