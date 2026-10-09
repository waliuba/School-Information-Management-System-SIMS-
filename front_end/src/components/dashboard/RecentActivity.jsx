import { useEffect, useState } from 'react';
import AnimatedSection from '../common/AnimatedSection.jsx';
import { getTodaysActivity } from '../../utils/activityLog.js';

export default function RecentActivity() {
  const [activity, setActivity] = useState(getTodaysActivity);

  useEffect(() => {
    const refresh = () => setActivity(getTodaysActivity());
    window.addEventListener('storage', refresh);
    return () => window.removeEventListener('storage', refresh);
  }, []);

  return (
    <AnimatedSection className="dashboard-section activity-placeholder">
      <div className="dashboard-section__header"><div><p className="dashboard-eyebrow">RECENT ACTIVITY</p><h2>Activity feed</h2></div></div>
      {activity ? (
        <div className="activity-empty activity-entry">
          <span aria-hidden="true">✓</span>
          <p><strong>{activity.action} {activity.resource}</strong><time dateTime={activity.completedAt}>{new Intl.DateTimeFormat(undefined, { hour: 'numeric', minute: '2-digit' }).format(new Date(activity.completedAt))}</time></p>
        </div>
      ) : (
        <div className="activity-empty"><span aria-hidden="true">◷</span><p>No completed tasks yet today. Completed work will appear here.</p></div>
      )}
    </AnimatedSection>
  );
}
