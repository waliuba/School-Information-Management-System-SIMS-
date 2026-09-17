import { AnimatePresence, motion } from 'framer-motion';
import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import welcomeAnimation from '../../assets/lottie/onboarding-welcome.json';
import studentsAnimation from '../../assets/lottie/onboarding-students.json';
import teachersAnimation from '../../assets/lottie/onboarding-teachers.json';
import schoolAnimation from '../../assets/lottie/onboarding-school.json';
import { pageTransition } from '../../animations/pageTransition.js';
import OnboardingControls from './components/OnboardingControls.jsx';
import OnboardingProgress from './components/OnboardingProgress.jsx';
import OnboardingSlide from './components/OnboardingSlide.jsx';

const slides = [
  {
    title: 'Welcome to SIMS',
    description: 'Use this React client to log in, call backend endpoints, and inspect Oracle-backed school data.',
    animationData: welcomeAnimation,
  },
  {
    title: 'Manage Students',
    description: 'Student pages will call /api/students through a dedicated student API service.',
    animationData: studentsAnimation,
  },
  {
    title: 'Manage Teachers and Subjects',
    description: 'Teacher, subject, and teacher-subject modules will mirror backend resources.',
    animationData: teachersAnimation,
  },
  {
    title: 'Track Attendance and Results',
    description: 'Attendance and results data should be validated and calculated by the backend.',
    animationData: schoolAnimation,
  },
];

export default function Onboarding() {
  const [currentIndex, setCurrentIndex] = useState(0);
  const navigate = useNavigate();
  const currentSlide = slides[currentIndex];
  const isLastSlide = currentIndex === slides.length - 1;

  function handleNext() {
    if (isLastSlide) {
      navigate('/login');
      return;
    }

    setCurrentIndex((index) => index + 1);
  }

  function handleBack() {
    setCurrentIndex((index) => Math.max(index - 1, 0));
  }

  return (
    <main className="onboarding-page">
      <section className="onboarding-panel">
        <AnimatePresence mode="wait">
          <motion.div key={currentSlide.title} {...pageTransition}>
            <OnboardingSlide slide={currentSlide} />
          </motion.div>
        </AnimatePresence>

        <OnboardingProgress total={slides.length} currentIndex={currentIndex} />
        <OnboardingControls
          canGoBack={currentIndex > 0}
          isLastSlide={isLastSlide}
          onBack={handleBack}
          onNext={handleNext}
          onSkip={() => navigate('/login')}
        />
      </section>
    </main>
  );
}
