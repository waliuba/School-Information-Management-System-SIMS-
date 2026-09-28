import { AnimatePresence, motion } from 'framer-motion';
import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import booksStackAnimation from '../../assets/lottie/Books stack.json';
import welcomeAnimation from '../../assets/lottie/Welcome Animation.json';
import teachersAnimation from '../../assets/lottie/Teamwork Gears.json';
import { pageTransition } from '../../animations/pageTransition.js';
import OnboardingControls from './components/OnboardingControls.jsx';
import OnboardingProgress from './components/OnboardingProgress.jsx';
import OnboardingSlide from './components/OnboardingSlide.jsx';

const slides = [
  {
   
  title: 'Welcome to SIMS',
  description:
    'SIMS is an evolving School Information Management System built with React, Spring Boot, and Oracle to explore how a complete school information platform is designed, developed, and connected from database to user interface.',
  animationData: welcomeAnimation,
},
 {
  title: 'Built as One System',
  description:
    'From student and teacher records to enrollment, academic performance, and school operations, SIMS brings related information together through a connected system architecture.',
  animationData: booksStackAnimation,
},
  {
  title: 'One System. Multiple Layers.',
  description:
    'SIMS connects the user interface, REST APIs, backend services, and Oracle database through a structured architecture where each layer has a clear responsibility.',
  animationData: teachersAnimation,
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
