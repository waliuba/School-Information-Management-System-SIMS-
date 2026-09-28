import { useCallback, useMemo, useState } from 'react';
import { Link } from 'react-router-dom';
import ModuleDataCard from '../../components/dashboard/ModuleDataCard.jsx';
import { useApi } from '../../hooks/useApi.js';
import { getAdminDashboardData } from '../../services/api/dashboardApi.js';

function Card({ title, subtitle, icon, action, children, className = '' }) {
  return (
    <section className={`admin-card ${className}`}>
      <header className="admin-card__header">
        {icon ? <span className="admin-card__icon" aria-hidden="true">{icon}</span> : null}
        <div>
          <h2>{title}</h2>
          {subtitle ? <p>{subtitle}</p> : null}
        </div>
        {action ? <Link className="admin-card__action" to={action.href}>{action.label}<span aria-hidden="true">→</span></Link> : null}
      </header>
      {children}
    </section>
  );
}

function DistributionChart({ data }) {
  const maximum = Math.max(...data.map((item) => Number(item.value) || 0), 1);
  return (
    <ul className="distribution-chart" aria-label="Performance result counts by score range">
      {data.map((item) => (
        <li className="distribution-chart__row" key={item.label}>
          <span className="distribution-chart__label">{item.label}</span>
          <span className="distribution-chart__track">
            <span
              className="distribution-chart__bar"
              style={{ width: `${(item.value / maximum) * 100}%`, backgroundColor: item.color }}
            />
          </span>
          <strong>{Number(item.value).toLocaleString()}</strong>
        </li>
      ))}
      {!data.length ? <li className="admin-empty">No score distribution is available.</li> : null}
    </ul>
  );
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

  return (
    <div className="donut-layout">
      <div className="admin-donut" style={{ background: `conic-gradient(${gradient})` }}>
        <div><strong>{total.toLocaleString()}</strong><span>students</span></div>
      </div>
      <ul className="chart-list">
        {data.map((item) => (
          <li key={item.label}>
            <span><i style={{ background: item.color }} />{item.label}</span>
            <strong>{item.value}</strong>
          </li>
        ))}
      </ul>
    </div>
  );
}

function getCount(summaryValue, records) {
  if (typeof summaryValue === 'number' && Number.isFinite(summaryValue)) {
    return summaryValue;
  }
  return Array.isArray(records) ? records.length : null;
}

function getBreakdown(records, field) {
  if (!Array.isArray(records)) return [];

  const counts = new Map();
  records.forEach((record) => {
    const label = record[field];
    if (typeof label === 'string' && label.trim()) {
      counts.set(label, (counts.get(label) || 0) + 1);
    }
  });

  return [...counts].map(([label, value]) => ({ label, value }));
}

function formatScore(value) {
  if (typeof value !== 'number' || !Number.isFinite(value)) return value;
  return new Intl.NumberFormat(undefined, { maximumFractionDigits: 2 }).format(value);
}

export default function AdminDashboard() {
  const loadDashboard = useCallback(() => getAdminDashboardData(), []);
  const { data, error, isLoading, execute } = useApi(loadDashboard, { refetchOnWindowFocus: true });
  const [studentSearch, setStudentSearch] = useState('');
  const retryDashboard = useCallback(() => {
    execute().catch((retryError) => console.error('Could not retry dashboard data request:', retryError));
  }, [execute]);
  const summary = data?.summary || {};
  const students = Array.isArray(data?.students) ? data.students : [];
  const departments = Array.isArray(data?.departments) ? data.departments : [];
  const performanceRows = Array.isArray(data?.performance?.rows) ? data.performance.rows : [];
  const departmentData = departments.map((department) => ({
    label: department.departmentName || 'Unnamed department',
    value: students.filter((student) => student.departmentId === department.departmentId).length,
  }));
  const statusData = [...new Set(students.map((student) => student.status).filter(Boolean))].map((status) => ({
    label: status,
    value: students.filter((student) => student.status === status).length,
    color: /active|complete|success/i.test(status)
      ? '#15803d'
      : /pending|late|wait/i.test(status)
        ? '#b45309'
        : /inactive|fail|absent|error/i.test(status)
          ? '#b91c1c'
          : '#64748b',
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
  const performanceAvailable = performanceRows.length > 0;
  const marksDistribution = Array.isArray(data?.performance?.marksDistribution)
    ? data.performance.marksDistribution
    : [];
  const moduleCards = [
    {
      title: 'Students',
      icon: 'users',
      description: 'Registered students',
      value: getCount(summary.totalStudents, data?.students),
      secondaryStats: getBreakdown(data?.students, 'status'),
      action: { label: 'View students', href: '/students' },
    },
    {
      title: 'Teachers',
      icon: 'graduation-cap',
      description: 'Teaching staff records',
      value: getCount(summary.totalTeachers, data?.teachers),
      action: { label: 'View teachers', href: '/teachers' },
    },
    {
      title: 'Courses',
      icon: 'book-open',
      description: 'Courses in the catalog',
      value: getCount(summary.totalSubjects, null),
      action: { label: 'View courses', href: '/courses' },
    },
    {
      title: 'Departments',
      icon: 'building',
      description: 'Academic departments',
      value: getCount(summary.totalDepartments, data?.departments),
      secondaryStats: departmentData.map(({ label, value }) => ({ label, value })),
      action: { label: 'View departments', href: '/departments' },
    },
    {
      title: 'Enrollments',
      icon: 'clipboard-list',
      description: 'Enrollment records',
      value: Array.isArray(data?.enrollments) ? data.enrollments.length : null,
      secondaryStats: getBreakdown(data?.enrollments, 'semester'),
      action: { label: 'View enrollments', href: '/enrollments' },
    },
    {
      title: 'Attendance',
      icon: 'calendar-check',
      description: 'Attendance records',
      value: Array.isArray(data?.attendance) ? data.attendance.length : null,
      secondaryStats: getBreakdown(data?.attendance, 'status'),
      action: { label: 'View attendance', href: '/attendance' },
    },
    {
      title: 'Examinations',
      icon: 'file-text',
      description: 'Examination result records',
      value: Array.isArray(data?.examinations) ? data.examinations.length : null,
      action: { label: 'View examinations', href: '/results' },
    },
    {
      title: 'Academic performance',
      icon: 'chart',
      description: 'Average score across recorded results',
      value: performanceAvailable ? summary.averageScore : null,
      valueLabel: formatScore(summary.averageScore),
      secondaryStats: performanceAvailable
        ? [
            { label: 'Pass rate', value: summary.passRate, suffix: '%' },
            { label: 'Highest score', value: summary.highestScore },
            { label: 'Lowest score', value: summary.lowestScore },
          ].filter(({ value }) => value !== null && value !== undefined)
        : [],
      action: { label: 'View performance', href: '/performance' },
    },
  ];
  const cardState = isLoading ? 'loading' : error ? 'error' : 'loaded';
  const departmentMaximum = Math.max(...departmentData.map((item) => item.value), 1);

  return (
    <div className="admin-dashboard">
      <div className="admin-dashboard__heading">
        <div>
          <span className="admin-eyebrow">SIMS ADMIN / SCHOOL OVERVIEW</span>
          <h1>Dashboard</h1>
          <p>Welcome back, Admin. Here is what is happening across SIMS.</p>
        </div>
      </div>

      <section className="admin-module-grid" aria-label="Live school data">
        {moduleCards.map((item) => (
          <ModuleDataCard
            key={item.title}
            {...item}
            state={cardState}
            errorMessage={error?.message}
            onRetry={retryDashboard}
          />
        ))}
      </section>

      {!isLoading && !error ? (
        <>
          <div className="admin-grid admin-grid--analytics">
            <Card
              title="Performance by score range"
              subtitle="Recorded results grouped into score bands"
              icon="▥"
              action={{ label: 'View results', href: '/results' }}
              className="admin-card--wide"
            >
              <div className="chart-summary">
                <strong>{performanceRows.length}</strong><span>performance records</span><b>Live database data</b>
              </div>
              <DistributionChart data={marksDistribution} />
            </Card>
            <Card
              title="Student status"
              subtitle="Current student records"
              icon="◎"
              action={{ label: 'View students', href: '/students' }}
            >
              <DonutChart data={statusData} />
            </Card>
            <Card
              title="Students by department"
              subtitle="Students linked to each department"
              icon="▦"
              action={{ label: 'View departments', href: '/departments' }}
            >
              {departmentData.length ? (
                <div className="bar-chart" role="img" aria-label="Students by department bar chart">
                  {departmentData.map((item) => (
                    <div className="bar-chart__row" key={item.label}>
                      <span>{item.label}</span>
                      <div><i style={{ width: `${(item.value / departmentMaximum) * 100}%` }} /></div>
                      <strong>{item.value}</strong>
                    </div>
                  ))}
                </div>
              ) : <p className="admin-empty">No department student records available.</p>}
            </Card>
            <Card
              title="Academic performance"
              subtitle="Summary metrics calculated by SIMS"
              icon="▤"
              action={{ label: 'View performance', href: '/performance' }}
            >
              {performanceAvailable ? (
                <div className="performance-metrics">
                  <div><strong>{formatScore(summary.averageScore) ?? '—'}</strong><span>Average score</span></div>
                  <div><strong>{formatScore(summary.passRate) ?? '—'}{summary.passRate != null ? '%' : ''}</strong><span>Pass rate</span></div>
                  <div><strong>{formatScore(summary.highestScore) ?? '—'}</strong><span>Highest score</span></div>
                </div>
              ) : <p className="admin-empty">No performance records are available.</p>}
            </Card>
          </div>

          <div className="admin-grid admin-grid--lower">
            <Card
              title="Recent students"
              subtitle="Latest records added to SIMS"
              icon="♙"
              action={{ label: 'View all', href: '/students' }}
              className="admin-card--wide"
            >
              <div className="table-toolbar">
                <label className="admin-search">
                  ⌕
                  <input
                    value={studentSearch}
                    onChange={(event) => setStudentSearch(event.target.value)}
                    placeholder="Search students..."
                    aria-label="Search recent students"
                  />
                </label>
              </div>
              <ul className="recent-students">
                {filteredStudents.map(([admissionNo, name, department, status]) => (
                  <li className="recent-students__item" key={admissionNo}>
                    <span className="recent-students__avatar" aria-hidden="true">
                      {name ? name.split(' ').map((part) => part[0]).join('').slice(0, 2).toUpperCase() : '—'}
                    </span>
                    <span className="recent-students__identity">
                      <strong>{name || 'Name unavailable'}</strong>
                      <small>{admissionNo || 'Admission number unavailable'} · {department}</small>
                    </span>
                    <span className={`status-badge status-badge--${status.toLowerCase()}`}>{status}</span>
                  </li>
                ))}
              </ul>
              {!filteredStudents.length
                ? <p className="admin-empty">No students found matching your search.</p>
                : null}
            </Card>
          </div>
        </>
      ) : null}
    </div>
  );
}
