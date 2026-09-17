import { defaultTransition } from './variants.js';

export const fadeIn = {
  hidden: {
    opacity: 0,
    y: 12,
  },
  visible: {
    opacity: 1,
    y: 0,
    transition: defaultTransition,
  },
};
