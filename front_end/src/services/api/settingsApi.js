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
