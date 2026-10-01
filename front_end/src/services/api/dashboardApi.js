import axiosClient from './axiosClient.js';

export async function getDashboardSummary() {
  const response = await axiosClient.get('/dashboard/summary');
  return response.data;
}

export async function getDashboardPerformance() {
  const response = await axiosClient.get('/dashboard/performance');
  return response.data;
}
