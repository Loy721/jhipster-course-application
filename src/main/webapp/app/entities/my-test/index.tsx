import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import MyTest from './my-test';
import MyTestDetail from './my-test-detail';
import MyTestUpdate from './my-test-update';
import MyTestDeleteDialog from './my-test-delete-dialog';

const MyTestRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<MyTest />} />
    <Route path="new" element={<MyTestUpdate />} />
    <Route path=":id">
      <Route index element={<MyTestDetail />} />
      <Route path="edit" element={<MyTestUpdate />} />
      <Route path="delete" element={<MyTestDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default MyTestRoutes;
