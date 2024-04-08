import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, getSortState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortUp, faSortDown } from '@fortawesome/free-solid-svg-icons';
import { ASC, DESC, SORT } from 'app/shared/util/pagination.constants';
import { overrideSortStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities } from './my-user-my-test.reducer';

export const MyUserMyTest = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [sortState, setSortState] = useState(overrideSortStateWithQueryParams(getSortState(pageLocation, 'id'), pageLocation.search));

  const myUserMyTestList = useAppSelector(state => state.myUserMyTest.entities);
  const loading = useAppSelector(state => state.myUserMyTest.loading);

  const getAllEntities = () => {
    dispatch(
      getEntities({
        sort: `${sortState.sort},${sortState.order}`,
      }),
    );
  };

  const sortEntities = () => {
    getAllEntities();
    const endURL = `?sort=${sortState.sort},${sortState.order}`;
    if (pageLocation.search !== endURL) {
      navigate(`${pageLocation.pathname}${endURL}`);
    }
  };

  useEffect(() => {
    sortEntities();
  }, [sortState.order, sortState.sort]);

  const sort = p => () => {
    setSortState({
      ...sortState,
      order: sortState.order === ASC ? DESC : ASC,
      sort: p,
    });
  };

  const handleSyncList = () => {
    sortEntities();
  };

  const getSortIconByFieldName = (fieldName: string) => {
    const sortFieldName = sortState.sort;
    const order = sortState.order;
    if (sortFieldName !== fieldName) {
      return faSort;
    } else {
      return order === ASC ? faSortUp : faSortDown;
    }
  };

  return (
    <div>
      <h2 id="my-user-my-test-heading" data-cy="MyUserMyTestHeading">
        <Translate contentKey="jhipsterCourseApp.myUserMyTest.home.title">My User My Tests</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="jhipsterCourseApp.myUserMyTest.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/my-user-my-test/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="jhipsterCourseApp.myUserMyTest.home.createLabel">Create new My User My Test</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {myUserMyTestList && myUserMyTestList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="jhipsterCourseApp.myUserMyTest.id">ID</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('grade')}>
                  <Translate contentKey="jhipsterCourseApp.myUserMyTest.grade">Grade</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('grade')} />
                </th>
                <th>
                  <Translate contentKey="jhipsterCourseApp.myUserMyTest.myUser">My User</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="jhipsterCourseApp.myUserMyTest.myTest">My Test</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {myUserMyTestList.map((myUserMyTest, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/my-user-my-test/${myUserMyTest.id}`} color="link" size="sm">
                      {myUserMyTest.id}
                    </Button>
                  </td>
                  <td>{myUserMyTest.grade}</td>
                  <td>{myUserMyTest.myUser ? <Link to={`/my-user/${myUserMyTest.myUser.id}`}>{myUserMyTest.myUser.id}</Link> : ''}</td>
                  <td>{myUserMyTest.myTest ? <Link to={`/my-test/${myUserMyTest.myTest.id}`}>{myUserMyTest.myTest.id}</Link> : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/my-user-my-test/${myUserMyTest.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/my-user-my-test/${myUserMyTest.id}/edit`}
                        color="primary"
                        size="sm"
                        data-cy="entityEditButton"
                      >
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        onClick={() => (window.location.href = `/my-user-my-test/${myUserMyTest.id}/delete`)}
                        color="danger"
                        size="sm"
                        data-cy="entityDeleteButton"
                      >
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="jhipsterCourseApp.myUserMyTest.home.notFound">No My User My Tests found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default MyUserMyTest;
