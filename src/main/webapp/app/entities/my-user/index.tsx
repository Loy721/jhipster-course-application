import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import MyUser from './my-user';
import MyUserDetail from './my-user-detail';
import MyUserUpdate from './my-user-update';
import MyUserDeleteDialog from './my-user-delete-dialog';

const MyUserRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<MyUser />} />
    <Route path="new" element={<MyUserUpdate />} />
    <Route path=":id">
      <Route index element={<MyUserDetail />} />
      <Route path="edit" element={<MyUserUpdate />} />
      <Route path="delete" element={<MyUserDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default MyUserRoutes;
