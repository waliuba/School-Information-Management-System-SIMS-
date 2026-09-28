import { lazy, Suspense } from 'react';
import { Navigate, Route, Routes } from 'react-router-dom';
import Loading from '../components/common/Loading.jsx';
import ProtectedRoute from './ProtectedRoute.jsx';
import RoleRoute from './RoleRoute.jsx';
import {
  getAttendance,
  getClasses,
  getCourses,
  getDepartments,
  getEnrollments,
  getPerformanceRows,
  getResults,
  getRoles,
  getTeachers,
  getUnits,
  getUsers,
} from '../services/api/resourcesApi.js';


const MainLayout = lazy(() => import('../components/layout/MainLayout.jsx'));
const Login = lazy(() => import('../pages/auth/Login.jsx'));
const Unauthorized = lazy(() => import('../pages/auth/Unauthorized.jsx'));
const Dashboard = lazy(() => import('../pages/dashboard/Dashboard.jsx'));
const Onboarding = lazy(() => import('../pages/onboarding/Onboarding.jsx'));
const LandingPage = lazy(() => import('../pages/landing/LandingPage.jsx'));
const ResourcePage = lazy(() => import('../pages/shared/ResourcePage.jsx'));

const PlaceholderPage = lazy(() => import('../pages/shared/PlaceholderPage.jsx'));

export default function AppRoutes() {
  return (
    <Suspense fallback={<Loading />}>
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
            <Route
  path="/students"
  element={
          <ResourcePage
            title="Students"
            description="Student records returned by the backend."
            request={getStudents}
          />
        }
      />
            
            <Route path="/classes" element={<ResourcePage title="Classes" description="Classes returned by the backend." request={getClasses} />} />
            
            <Route path="/subjects" element={<ResourcePage title="Subjects" description="Academic units returned by the backend." request={getUnits} />} />
            
            
            
            <Route path="/settings" element={<PlaceholderPage title="System settings" resourcePath="/settings" />} />
            <Route element={<RoleRoute allowedRoles={['ADMIN']} />}>
              <Route path="/users" element={<ResourcePage title="Users" description="Users returned by the backend." request={getUsers} />} />
              <Route path="/roles" element={<ResourcePage title="Roles" description="Roles returned by the backend." request={getRoles} />} />
            </Route>
          </Route>
        </Route>

        <Route path="*" element={<Navigate to="/dashboard" replace />} />
      </Routes>
    </Suspense>
  );
}
