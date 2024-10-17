import { BASEURL } from '@env';
import { getItem } from '../Storage/AsyncStorage';
import axios from 'axios';

/**
 * @description Axios 인스턴스를 생성
 * 목적 : axios 요청 시 header에 token을 보내기 위함
 */

const instance = axios.create({
  baseURL: BASEURL,
});

instance.interceptors.request.use(
  (config) => {
    const accessToken = getItem('JWT');

    try {
      if (accessToken) {
        config.headers['Authorization'] = `Bearer ${accessToken}`;
      }
      return config;
    } catch (err) {
      console.error('[_axios.interceptors.request] config : ' + err.message);
    }
    return config;
  },
  (error) => {
    // 요청 에러 직전 호출됩니다.
    return Promise.reject(error);
  },
);

export default instance;
