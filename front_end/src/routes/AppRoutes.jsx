import { Navigate, Route, Routes } from 'react-router-dom';
import MainLayout from '../components/layout/MainLayout.jsx';
import Login from '../pages/auth/Login.jsx';
import Unauthorized from '../pages/auth/Unauthorized.jsx';
import Dashboard from '../pages/dashboard/Dashboard.jsx';
import Onboarding from '../pages/onboarding/Onboarding.jsx';
import ResourcePage from '../pages/shared/ResourcePage.jsx';
import ProtectedRoute from './ProtectedRoute.jsx';
import RoleRoute from './RoleRoute.jsx';
import {
  getAttendance,
  getClasses,
  getDepartments,
  getEnrollments,
  getResults,
  getRoles,
  getStudents,
  getTeachers,
  getUnits,
  getUsers,
} from '../services/api/resourcesApi.js';

export default function AppRoutes() {
  return (
    <Routes>
      <Route path="/login" element={<Login />} />
      <Route path="/onboarding" element={<Onboarding />} />
      <Route path="/unauthorized" element={<Unauthorized />} />

      <Route element={<ProtectedRoute />}>
        <Route element={<MainLayout />}>
          <Route index element={<Navigate to="/dashboard" replace />} />
          <Route path="/dashboard" element={<Dashboard />} />
          <Route path="/students" element={<ResourcePage title="Students" description="Students returned by the backend." request={getStudents} />} />
          <Route path="/teachers" element={<ResourcePage title="Teachers" description="Teachers returned by the backend." request={getTeachers} />} />
          <Route path="/classes" element={<ResourcePage title="Classes" description="Classes returned by the backend." request={getClasses} />} />
          <Route path="/departments" element={<ResourcePage title="Departments" description="Departments returned by the backend." request={getDepartments} />} />
          <Route path="/subjects" element={<ResourcePage title="Subjects" description="Academic units returned by the backend." request={getUnits} />} />
          <Route path="/enrollments" element={<ResourcePage title="Enrollments" description="Enrollments returned by the backend." request={getEnrollments} />} />
          <Route path="/attendance" element={<ResourcePage title="Attendance" description="Attendance returned by the backend." request={getAttendance} />} />
          <Route path="/results" element={<ResourcePage title="Exams & Results" description="Exam results returned by the backend." request={getResults} />} />
          <Route element={<RoleRoute allowedRoles={['ADMIN']} />}>
            <Route path="/users" element={<ResourcePage title="Users" description="Users returned by the backend." request={getUsers} />} />
            <Route path="/roles" element={<ResourcePage title="Roles" description="Roles returned by the backend." request={getRoles} />} />
          </Route>
        </Route>
      </Route>

      <Route path="*" element={<Navigate to="/dashboard" replace />} />
    </Routes>
  );
}
