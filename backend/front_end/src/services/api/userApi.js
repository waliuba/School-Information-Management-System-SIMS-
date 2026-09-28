import axiosClient from './axiosClient.js';

export async function getUsers(params = {}) {
  const response = await axiosClient.get('/users', { params });
  return response.data;
}

export async function getTeachers() {
  const response = await axiosClient.get('/teachers');
  return response.data;
}
