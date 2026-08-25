import { motion, useReducedMotion } from 'framer-motion';
import { fadeIn } from '../../animations/fadeIn.js';
import { viewportOnce } from '../../animations/variants.js';

export default function AnimatedSection({
  as = 'section',
  className = '',
  children,
  variants = fadeIn,
  viewport = viewportOnce,
  ...props
}) {
  const reduceMotion = useReducedMotion();
  const MotionTag = motion[as] || motion.section;

  if (reduceMotion) {
    return (
      <section className={className} {...props}>
        {children}
      </section>
    );
  }

  return (
    <MotionTag
      className={className}
      initial="hidden"
      whileInView="visible"
      viewport={viewport}
      variants={variants}
      {...props}
    >
      {children}
    </MotionTag>
  );
}
