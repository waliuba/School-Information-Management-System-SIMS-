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
    description: 'A centralized School Information Management System designed to bring important academic and administrative information into one connected platform.',
    animationData: welcomeAnimation,
  },
  {
    title: 'Everything Connected',
    description: 'Students, teachers, courses, departments, enrollment, attendance, examinations, and academic performance — designed to work together.',
    animationData: studentsAnimation,
  },
  {
    title: 'One System. Multiple Layers.',
    description: 'From the React frontend through REST and Spring Boot business logic to the Oracle database, each layer has a clear role.',
    animationData: teachersAnimation,
  },
  {
    title: 'The System Is Growing',
    description: 'Core management is implemented. Integration, authentication, testing, and deployment are the next stages in the journey.',
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
      navigate('/dashboard');
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
          onSkip={() => navigate('/dashboard')}
        />
      </section>
    </main>
  );
}
