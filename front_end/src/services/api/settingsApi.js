import axiosClient from './axiosClient.js';

export async function getSchoolProfile() {
  const response = await axiosClient.get('/settings/school-profile');
  return response.data;
}

export async function updateSchoolProfile(profile) {
  const response = await axiosClient.put('/settings/school-profile', profile);
  return response.data;
}

export async function getAuditEvents() {
  const response = await axiosClient.get('/settings/audit');
  return response.data;
}

export async function getSystemSettings() {
  const response = await axiosClient.get('/settings/system');
  return response.data;
}

export async function createSystemSetting(setting) {
  const response = await axiosClient.post('/settings/system', setting);
  return response.data;
}

export async function updateSystemSetting(key, value) {
  const response = await axiosClient.put(`/settings/system/${encodeURIComponent(key)}`, { value });
  return response.data;
}

export async function deleteSystemSetting(key) {
  await axiosClient.delete(`/settings/system/${encodeURIComponent(key)}`);
}

export async function getSystemHealth() {
  const response = await axiosClient.get('/settings/system-health');
  return response.data;
}
