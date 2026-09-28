import { useCallback, useMemo, useState } from 'react';
import { Link } from 'react-router-dom';
import ErrorMessage from '../../components/common/ErrorMessage.jsx';
import Loading from '../../components/common/Loading.jsx';
import { useApi } from '../../hooks/useApi.js';
import { getAdminDashboardData } from '../../services/api/dashboardApi.js';

function Card({ title, subtitle, children, className = '' }) {
  return <section className={`admin-card ${className}`}><header className="admin-card__header"><div><h2>{title}</h2>{subtitle ? <p>{subtitle}</p> : null}</div></header>{children}</section>;
}

function TrendChart({ data }) {
  const values = data.map((item, index) => ({ label: String(index + 1), value: Number(item.averageScore || 0) }));
  const max = Math.max(...values.map((item) => item.value), 1);
  const points = values.map((item, index) => `${(index / Math.max(values.length - 1, 1)) * 100},${100 - (item.value / max) * 78 - 8}`).join(' ');
  return <div className="trend-chart" role="img" aria-label="Backend performance scores by returned record">
    <svg viewBox="0 0 100 100" preserveAspectRatio="none" aria-hidden="true"><path d={`M ${points}`} fill="none" stroke="currentColor" strokeWidth="2.5" vectorEffect="non-scaling-stroke" /><path d={`M ${points} L 100 100 L 0 100 Z`} fill="currentColor" opacity=".1" /></svg>
    <div className="trend-chart__labels">{values.slice(0, 10).map((item) => <span key={item.label}>{item.label}</span>)}</div>
  </div>;
}

function DonutChart({ data }) {
  const total = data.reduce((sum, item) => sum + item.value, 0);
  if (!total) return <p className="admin-empty">No student status data available.</p>;
  let cursor = 0;
  const gradient = data.map((item) => {
    const start = cursor;
    cursor += (item.value / total) * 360;
    return `${item.color} ${start}deg ${cursor}deg`;
  }).join(', ');
  return <div className="donut-layout"><div className="admin-donut" style={{ background: `conic-gradient(${gradient})` }}><div><strong>{total.toLocaleString()}</strong><span>students</span></div></div><ul className="chart-list">{data.map((item) => <li key={item.label}><span><i style={{ background: item.color }} />{item.label}</span><strong>{item.value}</strong></li>)}</ul></div>;
}

export default function AdminDashboard() {
  const loadDashboard = useCallback(() => getAdminDashboardData(), []);
  const { data, error, isLoading } = useApi(loadDashboard, { refetchOnWindowFocus: true });
  const [filters, setFilters] = useState({ year: '2026', semester: 'Current semester', department: 'All departments' });
  const [studentSearch, setStudentSearch] = useState('');
  const summary = data?.summary || {};
  const students = data?.students || [];
  const departments = data?.departments || [];
  const performanceRows = data?.performance?.rows || [];
  const departmentData = departments.map((department) => ({
    label: department.departmentName,
    value: students.filter((student) => student.departmentId === department.departmentId).length,
  })).filter((item) => item.value > 0);
  const statusData = [...new Set(students.map((student) => student.status).filter(Boolean))].map((status) => ({
    label: status,
    value: students.filter((student) => student.status === status).length,
    color: ['#8bbf62', '#5d8fd3', '#d79a58', '#aeb9c3'][Math.abs(status.length) % 4],
  }));
  const recentStudents = [...students]
    .sort((first, second) => String(second.admissionDate || '').localeCompare(String(first.admissionDate || '')))
    .slice(0, 5)
    .map((student) => [
      student.admissionNo,
      [student.firstName, student.middleName, student.lastName].filter(Boolean).join(' '),
      departments.find((department) => department.departmentId === student.departmentId)?.departmentName || 'Unassigned',
      student.status || 'Unknown',
    ]);
  const filteredStudents = useMemo(
    () => recentStudents.filter((student) => student.join(' ').toLowerCase().includes(studentSearch.toLowerCase())),
    [recentStudents, studentSearch]
  );
  const dashboardKpis = [
    { label: 'Total students', value: summary.totalStudents, change: 'From student records', tone: 'success', icon: '♙' },
    { label: 'Teaching staff', value: summary.totalTeachers, change: 'From teacher records', tone: 'success', icon: '♟' },
    { label: 'Courses', value: summary.totalSubjects, change: 'From course records', tone: 'info', icon: '▤' },
    { label: 'Departments', value: summary.totalDepartments, change: 'From department records', tone: 'warning', icon: '▦' },
    { label: 'Average score', value: summary.averageScore ?? '-', change: 'From performance records', tone: 'success', icon: '◷' },
    { label: 'Pass rate', value: summary.passRate ?? '-', change: 'Backend calculation', tone: 'info', icon: '▣' },
  ];
  const recentActivity = [];
  const dataLinks = [
    { label: 'Students', count: summary.totalStudents ?? students.length, detail: 'Registered learners', icon: '♙', to: '/students' },
    { label: 'Teachers', count: summary.totalTeachers ?? (data?.teachers || []).length, detail: 'Teaching staff', icon: '♟', to: '/teachers' },
    { label: 'Courses', count: summary.totalSubjects, detail: 'Academic catalog', icon: '▤', to: '/courses' },
    { label: 'Departments', count: summary.totalDepartments ?? departments.length, detail: 'Academic departments', icon: '▦', to: '/departments' },
    { label: 'Enrollments', count: Array.isArray(data?.enrollments) ? data.enrollments.length : null, detail: 'Enrollment records', icon: '▣', to: '/enrollments' },
    { label: 'Attendance', count: Array.isArray(data?.attendance) ? data.attendance.length : null, detail: 'Attendance records', icon: '◷', to: '/attendance' },
    { label: 'Performance', count: performanceRows.length, detail: 'Performance records', icon: '▥', to: '/performance' },
  ];
  if (isLoading) return <div className="admin-dashboard"><Loading message="Loading live dashboard data..." /></div>;
  if (error) return <div className="admin-dashboard"><ErrorMessage error={error} title="Could not load live dashboard data" /></div>;

  return <div className="admin-dashboard">
    <div className="admin-dashboard__heading">
      <div><span className="admin-eyebrow">SIMS ADMIN / SCHOOL OVERVIEW</span><h1>Dashboard</h1><p>Welcome back, Admin. Here is what is happening across SIMS.</p></div>
      <div className="admin-dashboard__context"><label>Academic year<select value={filters.year} onChange={(event) => setFilters({ ...filters, year: event.target.value })}><option>2026</option><option>2025</option></select></label><label>Semester<select value={filters.semester} onChange={(event) => setFilters({ ...filters, semester: event.target.value })}><option>Current semester</option><option>Semester 1</option><option>Semester 2</option></select></label></div>
    </div>

    <div className="admin-filter-bar"><span>Analytics filters</span><select value={filters.department} onChange={(event) => setFilters({ ...filters, department: event.target.value })}><option>All departments</option><option>Computing</option><option>Business</option><option>Education</option></select><button type="button" onClick={() => setFilters({ year: '2026', semester: 'Current semester', department: 'All departments' })}>Reset filters</button><small>Mock data · ready for dashboard API integration</small></div>

    <div className="admin-kpi-grid">{dashboardKpis.map((item) => <article className="admin-kpi" key={item.label}><div className={`admin-kpi__icon admin-kpi__icon--${item.tone}`}>{item.icon}</div><span>{item.label}</span><strong>{item.value}</strong><small className={`admin-kpi__change admin-kpi__change--${item.tone}`}>{item.change}</small></article>)}</div>

    <div className="admin-grid admin-grid--analytics">
      <Card title="Student performance distribution" subtitle="Scores returned by the backend" className="admin-card--wide"><div className="chart-summary"><strong>{performanceRows.length}</strong><span>performance records</span><b>Live database data</b></div>{performanceRows.length ? <TrendChart /> : <p className="admin-empty">No performance records available.</p>}</Card>
      <Card title="Student status" subtitle="Current student records"><DonutChart data={statusData} /></Card>
      <Card title="Students by department" subtitle="Distribution across academic departments"><div className="bar-chart" role="img" aria-label="Students by department bar chart">{departmentData.map((item) => <div className="bar-chart__row" key={item.label}><span>{item.label}</span><div><i style={{ width: `${(item.value / 342) * 100}%` }} /></div><strong>{item.value}</strong></div>)}</div></Card>
      <Card title="Academic performance" subtitle="Calculated from backend results"><div className="performance-metrics"><div><strong>{summary.averageScore ?? '-'}</strong><span>Average score</span></div><div><strong>{summary.passRate ?? '-'}</strong><span>Pass rate</span></div><div><strong>{summary.highestScore ?? '-'}</strong><span>Highest score</span></div></div><p className="admin-empty">Comparative trends require dated historical performance records.</p></Card>
    </div>

    <div className="admin-grid admin-grid--lower">
      <Card title="Recent students" subtitle="Latest records added to SIMS" className="admin-card--wide"><div className="table-toolbar"><label className="admin-search">⌕<input value={studentSearch} onChange={(event) => setStudentSearch(event.target.value)} placeholder="Search students..." aria-label="Search recent students" /></label><Link to="/students">View all students →</Link></div><div className="admin-table-wrap"><table className="admin-table"><thead><tr><th>Admission no.</th><th>Name</th><th>Department</th><th>Status</th></tr></thead><tbody>{filteredStudents.map((student) => <tr key={student[0]}>{student.map((value, index) => <td key={`${student[0]}-${index}`}>{index === 3 ? <span className={`status-badge status-badge--${value.toLowerCase()}`}>{value}</span> : value}</td>)}</tr>)}</tbody></table>{!filteredStudents.length ? <p className="admin-empty">No students found matching your search.</p> : null}</div></Card>
      <Card title="Quick actions" subtitle="Common administration workflows"><div className="quick-actions">{[['Add student', '/students'], ['Add teacher', '/teachers'], ['Create enrollment', '/enrollments'], ['Upload results', '/results'], ['Generate report', '/results']].map(([label, to]) => <Link key={label} to={to}><span>＋</span>{label}<b>→</b></Link>)}</div></Card>
    </div>

    <div className="admin-grid admin-grid--footer">
      <Card title="Recent activity" subtitle="Latest actions across the system"><div className="activity-timeline">{recentActivity.map(([title, detail, time, icon]) => <div className="activity-item" key={title}><span className="activity-item__icon">{icon}</span><div><strong>{title}</strong><p>{detail}</p><small>{time}</small></div></div>)}</div></Card>
      <Card title="Data management" subtitle="Explore modules and their live record totals"><div className="data-links">{dataLinks.map(({ label, count, detail, icon, to }) => <Link to={to} className="data-link-card" key={label}><span className="data-link-card__top"><i className="data-link-card__icon" aria-hidden="true">{icon}</i><span className="data-link-card__identity"><strong>{label}</strong><small>{detail}</small></span><b aria-hidden="true">↗</b></span><span className="data-link-card__total">{count == null ? '—' : Number(count).toLocaleString()}<small>{count == null ? 'Data unavailable' : 'records'}</small></span></Link>)}</div></Card>
      <Card title="System health" subtitle="Live checks will connect to the backend"><div className="health-list">{['Backend API', 'Database', 'Authentication', 'Frontend'].map((label) => <div key={label}><span>{label}</span><strong><i />Not yet connected</strong></div>)}</div></Card>
    </div>
  </div>;
}
