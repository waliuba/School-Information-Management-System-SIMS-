import axiosClient from './axiosClient.js';

function unwrapList(response) {
  const payload = response.data;
  if (Array.isArray(payload)) {
    return payload;
  }
  return Array.isArray(payload?.data) ? payload.data : [];
}

async function getList(path, params) {
  const response = await axiosClient.get(path, { params });
  return unwrapList(response);
}

async function send(method, path, data) {
  const response = await axiosClient[method](path, data);
  return response.data?.data ?? response.data;
}

export const createStudent = (data) => send('post', '/students', data);
export const updateStudent = (id, data) => send('put', `/students/${id}`, data);
export const deleteStudent = (id) => send('delete', `/students/${id}`);
export const getStudent = (id) => axiosClient.get(`/students/${id}`).then((r) => r.data?.data ?? r.data);
export const createTeacher = (data) => send('post', '/teachers', data);
export const updateTeacher = (id, data) => send('put', `/teachers/${id}`, data);
export const deleteTeacher = (id) => send('delete', `/teachers/${id}`);
export const getTeacher = (id) => axiosClient.get(`/teachers/${id}`).then((r) => r.data?.data ?? r.data);
export const createCourse = (data) => send('post', '/courses', data);
export const updateCourse = (id, data) => send('put', `/courses/${id}`, data);
export const deleteCourse = (id) => send('delete', `/courses/${id}`);
export const getCourse = (id) => axiosClient.get(`/courses/${id}`).then((r) => r.data?.data ?? r.data);
export const createEnrollment = (data) => send('post', '/enrollments', data);
export const updateEnrollment = (id, data) => send('put', `/enrollments/${id}`, data);
export const deleteEnrollment = (id) => send('delete', `/enrollments/${id}`);
export const getEnrollment = (id) => axiosClient.get(`/enrollments/${id}`).then((r) => r.data?.data ?? r.data);
export const createResult = (data) => send('post', '/results', data);
export const updateResult = (id, data) => send('put', `/results/${id}`, data);
export const deleteResult = (id) => send('delete', `/results/${id}`);
export const createAttendance = (data) => send('post', '/attendance', data);
export const updateAttendance = (id, data) => send('put', `/attendance/${id}`, data);
export async function uploadResults(file) {
  const form = new FormData(); form.append('file', file);
  const response = await axiosClient.post('/results/upload', form, { headers: { 'Content-Type': 'multipart/form-data' } });
  return response.data?.data ?? response.data;
}

export function getStudents(params) {
  return getList('/students', params);
}

export function getTeachers(params) {
  return axiosClient.get('/teachers', { params }).then((response) => {
    const payload = response.data;
    if (Array.isArray(payload)) return payload;
    if (Array.isArray(payload?.data)) return payload.data;
    // Do not turn an unexpected API envelope into a misleading empty state.
    throw new Error('The teacher API returned an unexpected response format.');
  });
}

export function getClasses(params) {
  return getList('/classes', params);
}

export function getDepartments(params) {
  return getList('/departments', params);
}

export function getCourses(params) {
  return getList('/courses', params);
}

export function getUnits(params) {
  return getList('/units', params);
}

export function getEnrollments(params) {
  return getList('/enrollments', params);
}

export function getAttendance(params) {
  return getList('/attendance', params);
}

export function getMyAttendance() {
  return getList('/attendance/my');
}

export function getAttendanceByDate(date) {
  return getList(`/attendance/date/${date}`);
}

export function getResults(params) {
  return getList('/results', params);
}

export function getUsers(params) {
  return getList('/users', params);
}

export function getRoles(params) {
  return getList('/roles', params);
}
