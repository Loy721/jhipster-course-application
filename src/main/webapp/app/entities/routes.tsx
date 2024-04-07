import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import File from './file';
import MyTest from './my-test';
import Question from './question';
import MyUser from './my-user';
import MyUserMyTest from './my-user-my-test';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default () => {
  return (
    <div>
      <ErrorBoundaryRoutes>
        {/* prettier-ignore */}
        <Route path="file/*" element={<File />} />
        <Route path="my-test/*" element={<MyTest />} />
        <Route path="question/*" element={<Question />} />
        <Route path="my-user/*" element={<MyUser />} />
        <Route path="my-user-my-test/*" element={<MyUserMyTest />} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </ErrorBoundaryRoutes>
    </div>
  );
};
