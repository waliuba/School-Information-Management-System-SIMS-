import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import booksStackAnimation from '../../assets/lottie/Books stack.json';
import LottieAnimation from '../../components/common/LottieAnimation.jsx';

const progress = [
  {
    label: 'Oracle & database',
    value: 85,
    color: 'oracle',
  },
  {
    label: 'Spring Boot backend',
    value: 80,
    color: 'backend',
  },
  {
    label: 'Frontend',
    value: 40,
    color: 'frontend',
  },
  {
    label: 'Integration',
    value: 30,
    color: 'integration',
  },
  {
    label: 'DevOps',
    value: 25,
    color: 'devops',
  },
];

const journey = [
 
  {
    number: '01',
    title: 'Idea',
    text: 'SIMS started as an idea to build a centralized School Information Management System that could bring student records, classes, teachers, courses, enrollments, academic performance, attendance, and administration into one system.'
  },

  {
    number: '02',
    title: 'Planning',
    text: 'The project was broken down into an Oracle database, Spring Boot backend, and React frontend. We designed the database tables and relationships, defined the backend layers, planned DTOs and API endpoints, and mapped how the frontend would communicate with the backend.'
  },

  {
    number: '03',
    title: 'Architecture',
    text: 'SIMS evolved into a layered application where React handles the user interface, Spring Boot manages controllers, DTOs, mappers, services, repositories, and business logic, while Oracle 21c stores and enforces the data through relationships and constraints.'
  },

  {
    number: '04',
    title: 'Implementation',
    text: 'The backend has progressed through students, classes, teachers, departments, courses, units, enrollments, results, performance, and the supporting DTO, mapper, service, repository, and controller layers. Attendance, authentication, frontend integration, Docker, testing, CI/CD, and deployment are the next stages.'
  }
];


const modules = [
  [
    'Student management',
    'Student registration, admission-number lookup, class assignment, status, and search',
    'COMPLETED'
  ],
  [
    'Teacher management',
    'Teacher records with teacher numbers, contact details, and backend API structure',
    'COMPLETED'
  ],
  [
    'Courses & departments',
    'Course, unit, and department structures with Oracle relationships and constraints',
    'COMPLETED'
  ],
  [
    'Enrollment',
    'Student enrollment linked to courses and departments with validation and relationships',
    'COMPLETED'
  ],
  [
    'Performance & results',
    'Student scores, grades, marks validation, and academic performance records',
    'COMPLETED'
  ],
  [
    'Attendance',
    'Student attendance structure currently being developed as part of the academic management features',
    'IN PROGRESS'
  ],
  [
    'React frontend',
    'Frontend foundation, component structure, Vite setup, animations, Lottie integration, and interface development',
    'IN PROGRESS'
  ],
  [
    'Authentication',
    'User structure and JWT authentication foundation being developed for secure system access',
    'IN PROGRESS'
  ],
  [
    'DevOps',
    'WSL 2, Ubuntu, Docker, containerization, and the foundations for CI/CD and deployment',
    'IN PROGRESS'
  ],
  [
    'Backend foundation',
    'DTOs, MapStruct mappers, repositories, services, controllers, validation, and error handling',
    'COMPLETED'
  ],
  [
    'Database foundation',
    'Oracle 21c schema, relationships, foreign keys, constraints, identity columns, and JPA/Hibernate integration',
    'COMPLETED'
  ],
];
const challenges = [
  [
    'Oracle',
    'Worked through listener configuration, Oracle users and schemas, table relationships, constraints, invalid identifiers, missing tables, and database-to-application mapping.'
  ],

  [
    'Spring Boot',
    'Solved JPA and Hibernate mapping issues, repository queries, DTO and entity boundaries, validation, relationships, and application port conflicts.'
  ],

  [
    'Frontend',
    'Worked through Vite and Node compatibility, dependency issues, React setup, Lottie integration, and the transition from backend APIs to the frontend interface.'
  ],

  [
    'Environment',
    'Set up and connected Windows, WSL 2, Ubuntu, VS Code, Java, Maven, and Docker while learning how the development environment affects the application.'
  ],
];

const learning = [
  [
    'Java + Spring Boot',
    'OOP · REST APIs · Dependency Injection · JPA/Hibernate · DTOs · MapStruct · Validation'
  ],

  [
    'Oracle Database',
    'SQL · Schema Design · Relationships · Constraints · Identity Columns · Debugging'
  ],

  [
    'Frontend',
    'React · Vite · Components · API Integration · Lottie · Scroll Animations'
  ],

  [
    'DevOps',
    'Git · GitHub · WSL 2 · Docker · Containers · CI/CD Fundamentals'
  ],
];

function Placeholder({ label = 'Lottie animation placeholder', tone = 'blue' }) {
  return (
    <div className={`lottie-placeholder lottie-placeholder--${tone}`}>
      <LottieAnimation animationData={booksStackAnimation} className="books-stack-animation" />
      <p>{label}</p>
    </div>
  );
}

function Reveal({ children, className = '' }) {
  const [visible, setVisible] = useState(false);
  useEffect(() => {
    const node = document.querySelectorAll('[data-reveal]');
    const observer = new IntersectionObserver((entries) => entries.forEach((entry) => {
      if (entry.isIntersecting) {
        entry.target.classList.add('is-visible');
        observer.unobserve(entry.target);
      }
    }), { threshold: 0.12 });
    node.forEach((item) => observer.observe(item));
    return () => observer.disconnect();
  }, []);
  return <div data-reveal className={`reveal ${className}`}>{children}</div>;
}

function SectionHeading({ eyebrow, title, text }) {
  return <div className="section-heading">
    <span className="eyebrow">{eyebrow}</span>
    <h2>{title}</h2>
    {text && <p>{text}</p>}
  </div>;
}

export default function LandingPage() {
  const [activeChallenge, setActiveChallenge] = useState(0);
  const [menuOpen, setMenuOpen] = useState(false);

  const scrollTo = (id) => {
    document.getElementById(id)?.scrollIntoView({ behavior: 'smooth' });
    setMenuOpen(false);
  };

  return (
    <main className="landing">
      <nav className="landing-nav">
        <button className="brand" onClick={() => scrollTo('overview')} aria-label="Go to overview">
          <span className="brand-mark">S</span><span>SIMS<span className="brand-dot">.</span></span>
        </button>
        <button className="mobile-toggle" onClick={() => setMenuOpen(!menuOpen)} aria-expanded={menuOpen}>☰</button>
        <div className={`nav-links ${menuOpen ? 'nav-links--open' : ''}`}>
          {['overview', 'journey', 'architecture', 'progress', 'challenges', 'learning', 'roadmap'].map((id) => (
            <button key={id} onClick={() => scrollTo(id)}>{id[0].toUpperCase() + id.slice(1)}</button>
          ))}
          <Link className="nav-enter" to="/login">Enter system <span>↗</span></Link>
        </div>
      </nav>

      <section id="overview" className="hero section-shell">
        <div className="hero-copy">
          <div className="status-pill"><span /> In active development · 2026</div>
          <h1>A School System,<br /><em>built from the ground up.</em></h1>
          <p className="hero-lede">SIMS connects the people, academic data, and processes that keep a school moving.</p>
          <div className="hero-actions">
            <Link className="button button--dark" to="/login">Explore SIMS system <span>→</span></Link>
            <button className="text-button" onClick={() => scrollTo('journey')}>View project journey <span>↓</span></button>
          </div>
          <div className="hero-meta"><span>01</span><i /> <span>From idea to implementation</span></div>
        </div>
        <Reveal className="hero-visual"><Placeholder label="" tone="blue" />
          <div className="float-card float-card--top"><span className="mini-icon">↗</span><div><strong>Connected by design</strong><small>One platform · many workflows</small></div></div>
          <div className="float-card float-card--bottom"><span className="live-dot" /> <strong>System status</strong><small>Building the next layer</small></div>
        </Reveal>
      </section>

      <section id="journey" className="journey section-shell section-shell--stack">
        <SectionHeading eyebrow="01 / The journey" title={<>A project with a<br /><em>point of view.</em></>} text="SIMS is more than a collection of screens. It is a deliberate evolution from a real problem to a dependable software system." />
        <div className="journey-grid">
          {journey.map((item, index) => <Reveal key={item.number} className="journey-card">
            <span className={`journey-number ${index === journey.length - 1 ? 'journey-number--active' : ''}`}>{item.number}</span>
            <h3>{item.title}</h3><p>{item.text}</p><span className="card-arrow">↗</span>
          </Reveal>)}
        </div>
      </section>

      <section id="architecture" className="architecture section-shell split-section">
        <Reveal className="architecture-visual"><div className="diagram">
          {['React Frontend', 'REST API', 'Spring Boot', 'Service layer', 'Repositories', 'Oracle 21c'].map((layer, index) => <div className="diagram-row" key={layer}><span className="diagram-node">{layer}</span>{index < 5 && <span className="diagram-line">↓</span>}</div>)}
        </div></Reveal>
       <div className="section-copy">
  <span className="eyebrow">02 / Architecture</span>

  <h2>
    Layers that make<br />
    <em>SIMS easier to build.</em>
  </h2>

  <p>
    SIMS separates the frontend, API, business logic, data access, and
    database into clear layers. React handles the interface, while Spring
    Boot connects controllers, DTOs, mappers, services, and repositories
    to Oracle through JPA and Hibernate.
  </p>

  <div className="architecture-note">
    <span>↳</span>
    <strong>Built to evolve</strong>
    <small>
      Each layer has a clear responsibility, making new modules easier
      to develop, test, and maintain.
    </small>
  </div>
</div>
      </section>

      <section className="lifecycle section-shell section-shell--stack">
       <SectionHeading
  eyebrow="03 / Development lifecycle"
  title={
    <>
      From first idea to<br />
      <em>the next stage.</em>
    </>
  }
/>

<div className="lifecycle-line">
  {[
    'Idea',
    'Requirements',
    'Oracle design',
    'Spring Boot',
    'Backend architecture',
    'REST APIs',
    'React frontend',
    'Integration',
    'Docker & CI/CD',
    'Deployment',
  ].map((stage, index) => (
    <Reveal
      key={stage}
      className="lifecycle-step"
    >
      <span>{String(index + 1).padStart(2, '0')}</span>
      <i />
      <strong>{stage}</strong>
    </Reveal>
  ))}
</div>
      </section>

      <section
  id="progress"
  className="progress-section section-shell split-section split-section--reverse"
>
  <div className="section-copy">
    <span className="eyebrow">
      04 / Current signal
    </span>

    <h2>
      Progress you can<br />
      <em>see and understand.</em>
    </h2>

    <p>
      These progress indicators represent the current development stage of
      SIMS. They are project estimates, not automated metrics, and will
      change as new modules and capabilities are completed.
    </p>

    <Link
      className="text-button"
      to="/login"
    >
      See the system intro <span>→</span>
    </Link>
  </div>

  <Reveal className="progress-panel">
    <div className="panel-top">
      <span>SIMS DEVELOPMENT</span>
      <span className="panel-live">
        ● CURRENT STATE
      </span>
    </div>

    {progress.map((item) => (
      <div
        className="progress-row"
        key={item.label}
      >
        <div>
          <span>{item.label}</span>
          <strong>{item.value}%</strong>
        </div>

        <div className="progress-track">
          <i
            className={`progress-fill progress-fill--${item.color}`}
            style={{
              width: `${item.value}%`
            }}
          />
        </div>
      </div>
    ))}
  </Reveal>
</section>


     <section className="completed section-shell section-shell--stack">
  <SectionHeading
    eyebrow="05 / What is working"
    title={
      <>
        A system growing<br />
        <em>module by module.</em>
      </>
    }
    text="The core database and backend foundation are established, while the frontend, attendance, authentication, and DevOps layers continue to move through development."
  />

  <div className="module-grid">
    {modules.map(([title, text, status = 'COMPLETED']) => (
      <Reveal
        className="module-card"
        key={title}
      >
        <span className="check">
          {status === 'IN PROGRESS' ? '◐' : '✓'}
        </span>

        <div>
          <small>{status}</small>
          <h3>{title}</h3>
          <p>{text}</p>
        </div>
      </Reveal>
    ))}
  </div>
</section>
    
          <section
        id="challenges"
        className="challenges section-shell split-section"
      >
        <Reveal className="challenge-visual">
          <div className="challenge-number">
            0{activeChallenge + 1}
          </div>

          <span className="eyebrow">
            The problem behind the progress
          </span>

          <h3>
            {challenges[activeChallenge][0]}
          </h3>

          <p>
            {challenges[activeChallenge][1]}
          </p>

          <div className="challenge-flow">
            <span>Problem</span>
            <i>→</i>
            <span>Investigation</span>
            <i>→</i>
            <span>Solution</span>
          </div>
        </Reveal>

        <div className="section-copy">
          <span className="eyebrow">
            06 / Challenges
          </span>

          <h2>
            Problems became<br />
            <em>part of the learning.</em>
          </h2>

          <p>
            Building SIMS meant working through real problems across Oracle,
            Spring Boot, React, and the development environment. Each issue
            forced me to understand how the different layers of the system
            actually work together.
          </p>

          <div className="challenge-tabs">
            {challenges.map((challenge, index) => (
              <button
                className={
                  activeChallenge === index
                    ? 'is-active'
                    : ''
                }
                onClick={() => setActiveChallenge(index)}
                key={challenge[0]}
              >
                <span>
                  0{index + 1}
                </span>

                {challenge[0]}

                <b>↗</b>
              </button>
            ))}
          </div>
        </div>
      </section>

      <section
  id="learning"
  className="learning section-shell section-shell--stack"
>
  <SectionHeading
    eyebrow="07 / What was learned"
    title={
      <>
        A developer progression<br />
        <em>built through SIMS.</em>
      </>
    }
  />

  <div className="learning-grid">
    {learning.map(([title, text], index) => (
      <Reveal
        className="learning-card"
        key={title}
      >
        <span>
          0{index + 1}
        </span>

        <h3>{title}</h3>

        <p>{text}</p>

        <div className="learning-bar">
          <i
            style={{
              width: `${[90, 82, 65, 50][index]}%`
            }}
          />
        </div>
      </Reveal>
    ))}
  </div>
</section>

      <section
  id="roadmap"
  className="roadmap section-shell split-section split-section--reverse"
>
  <div className="section-copy">
    <span className="eyebrow">
      08 / Where SIMS goes next
    </span>

    <h2>
      Built today.<br />
      <em>Still evolving.</em>
    </h2>

        <p>
      The core database, Spring Boot backend, and React frontend are in place.
      The project is now moving into the DevOps stage, where Docker, containerization,
      CI/CD, testing, and deployment are being introduced to make SIMS easier to
      build, run, and deliver.
    </p>

    <Link
      className="button button--dark"
      to="/login"
    >
      Enter SIMS <span>→</span>
    </Link>
  </div>

  <div className="roadmap-list">
    {[
      ['COMPLETED', 'Core database & backend foundation'],
      ['COMPLETED', 'React frontend foundation'],
      ['IN PROGRESS', 'DevOps, Docker & containerization'],
      ['NEXT', 'CI/CD, testing & deployment'],
    ].map(([status, title], index) => (
      <div
        className={`roadmap-item roadmap-item--${index}`}
        key={status}
      >
        <span className="roadmap-status">
          {status}
        </span>

        <strong>
          {title}
        </strong>

        <span>
          0{index + 1}
        </span>
      </div>
    ))}
  </div>
</section>

<footer className="landing-footer">
  <div className="brand">
    <span className="brand-mark">S</span>
    <span>
      SIMS<span className="brand-dot">.</span>
    </span>
  </div>

  <p>
    Independently designed and developed by Xavier Waliuba as a
    learning and portfolio project for demonstrating software
    development skills to recruiters.
  </p>

  <span className="footer-year">
    © 2026 Xavier Waliuba · Learning & Recruitment Project · Non-commercial
  </span>
</footer>
</main>
  );
}
