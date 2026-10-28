import { Navigate, Route, Routes } from 'react-router-dom';
import MainLayout from '../components/layout/MainLayout.jsx';
import Login from '../pages/auth/Login.jsx';
import Unauthorized from '../pages/auth/Unauthorized.jsx';
import Dashboard from '../pages/dashboard/Dashboard.jsx';
import Onboarding from '../pages/onboarding/Onboarding.jsx';
import LandingPage from '../pages/landing/LandingPage.jsx';
import ResourcePage from '../pages/shared/ResourcePage.jsx';
import PlaceholderPage from '../pages/shared/PlaceholderPage.jsx';
import CrudModulePage from '../pages/shared/CrudModulePage.jsx';
import ResultsPage from '../pages/shared/ResultsPage.jsx';
import ReportsPage from '../pages/shared/ReportsPage.jsx';
import AttendancePage from '../pages/shared/AttendancePage.jsx';
import MarkAttendancePage from '../pages/shared/MarkAttendancePage.jsx';
import TeachersPage from '../pages/teachers/TeachersPage.jsx';
import ProfilePage from '../pages/account/ProfilePage.jsx';
import AccountSettingsPage from '../pages/account/AccountSettingsPage.jsx';
import SecurityPage from '../pages/account/SecurityPage.jsx';
import SettingsPage from '../pages/settings/SettingsPage.jsx';
import ProtectedRoute from './ProtectedRoute.jsx';
import RoleRoute from './RoleRoute.jsx';
import {
  getClasses,
  getDepartments,
  getEnrollments,
  getResults,
  getRoles,
  getStudents,
  getTeachers,
  getUnits,
} from '../services/api/resourcesApi.js';

export default function AppRoutes() {
  return (
    <Routes>
      <Route path="/" element={<LandingPage />} />
      <Route path="/login" element={<Login />} />
      <Route path="/forgot-password" element={<PlaceholderPage title="Forgot password" resourcePath="/auth/forgot-password" />} />
      <Route path="/signup" element={<PlaceholderPage title="Create an account" resourcePath="/auth/register" />} />
      <Route path="/unauthorized" element={<Unauthorized />} />

      <Route element={<ProtectedRoute />}>
        <Route path="/onboarding" element={<Onboarding />} />
        <Route element={<MainLayout />}>
          <Route path="/app" element={<Navigate to="/dashboard" replace />} />
          <Route path="/dashboard" element={<Dashboard />} />
          <Route path="/profile" element={<ProfilePage />} />
          <Route path="/account-settings" element={<AccountSettingsPage />} />
          <Route path="/security" element={<SecurityPage />} />
          <Route element={<RoleRoute allowedRoles={['ADMIN', 'SUPER_ADMIN']} />}>
            <Route path="/settings" element={<Navigate to="/settings/account" replace />} />
            {['account', 'security', 'school', 'notifications', 'preferences', 'data', 'system', 'audit', 'danger'].map((section) => <Route key={section} path={`/settings/${section}`} element={<SettingsPage />} />)}
          </Route>
          <Route path="/notifications" element={<PlaceholderPage title="Notifications" resourcePath="/dashboard/activity" />} />
          <Route path="/students" element={<CrudModulePage module="students" />} />
          <Route path="/students/new" element={<CrudModulePage module="students" />} />
          <Route path="/students/:id" element={<CrudModulePage module="students" />} />
          <Route path="/students/:id/edit" element={<CrudModulePage module="students" />} />
          <Route element={<RoleRoute allowedRoles={['ADMIN', 'SUPER_ADMIN']} />}>
            <Route path="/teachers" element={<TeachersPage />} />
            <Route path="/teachers/new" element={<CrudModulePage module="teachers" />} />
            <Route path="/teachers/:id" element={<CrudModulePage module="teachers" />} />
            <Route path="/teachers/:id/edit" element={<CrudModulePage module="teachers" />} />
          </Route>
          <Route path="/classes" element={<ResourcePage title="Classes" description="Classes returned by the backend." request={getClasses} />} />
          <Route path="/departments" element={<ResourcePage title="Departments" description="Departments returned by the backend." request={getDepartments} />} />
          <Route path="/subjects" element={<ResourcePage title="Subjects" description="Academic units returned by the backend." request={getUnits} />} />
          <Route path="/enrollments" element={<CrudModulePage module="enrollments" />} />
          <Route path="/enrollments/new" element={<CrudModulePage module="enrollments" />} />
          <Route path="/enrollments/:id" element={<CrudModulePage module="enrollments" />} />
          <Route path="/enrollments/:id/edit" element={<CrudModulePage module="enrollments" />} />
          <Route path="/attendance" element={<AttendancePage />} />
          <Route element={<RoleRoute allowedRoles={['TEACHER']} />}>
            <Route path="/attendance/mark" element={<MarkAttendancePage />} />
          </Route>
          <Route path="/courses" element={<CrudModulePage module="courses" />} />
          <Route path="/courses/new" element={<CrudModulePage module="courses" />} />
          <Route path="/courses/:id" element={<CrudModulePage module="courses" />} />
          <Route path="/courses/:id/edit" element={<CrudModulePage module="courses" />} />
          <Route path="/results" element={<Navigate to="/results/upload" replace />} />
          <Route path="/results/upload" element={<ResultsPage />} />
          <Route path="/results/:id/edit" element={<ResultsPage />} />
          <Route path="/results/:id" element={<ResultsPage />} />
          <Route path="/reports" element={<ReportsPage />} />
          <Route element={<RoleRoute allowedRoles={['ADMIN', 'SUPER_ADMIN']} />}>
            <Route path="/users" element={<CrudModulePage module="users" />} />
            <Route path="/users/new" element={<CrudModulePage module="users" />} />
            <Route path="/users/:id" element={<CrudModulePage module="users" />} />
            <Route path="/users/:id/edit" element={<CrudModulePage module="users" />} />
            <Route path="/roles" element={<ResourcePage title="Roles" description="Roles returned by the backend." request={getRoles} />} />
          </Route>
        </Route>
      </Route>

      <Route path="*" element={<Navigate to="/dashboard" replace />} />
    </Routes>
  );
}
