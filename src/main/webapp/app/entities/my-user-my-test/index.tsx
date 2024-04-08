import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import MyUserMyTest from './my-user-my-test';
import MyUserMyTestDetail from './my-user-my-test-detail';
import MyUserMyTestUpdate from './my-user-my-test-update';
import MyUserMyTestDeleteDialog from './my-user-my-test-delete-dialog';

const MyUserMyTestRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<MyUserMyTest />} />
    <Route path="new" element={<MyUserMyTestUpdate />} />
    <Route path=":id">
      <Route index element={<MyUserMyTestDetail />} />
      <Route path="edit" element={<MyUserMyTestUpdate />} />
      <Route path="delete" element={<MyUserMyTestDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default MyUserMyTestRoutes;
