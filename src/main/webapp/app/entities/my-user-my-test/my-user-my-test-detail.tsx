import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './my-user-my-test.reducer';

export const MyUserMyTestDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const myUserMyTestEntity = useAppSelector(state => state.myUserMyTest.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="myUserMyTestDetailsHeading">
          <Translate contentKey="jhipsterCourseApp.myUserMyTest.detail.title">MyUserMyTest</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{myUserMyTestEntity.id}</dd>
          <dt>
            <span id="grade">
              <Translate contentKey="jhipsterCourseApp.myUserMyTest.grade">Grade</Translate>
            </span>
          </dt>
          <dd>{myUserMyTestEntity.grade}</dd>
          <dt>
            <Translate contentKey="jhipsterCourseApp.myUserMyTest.myUser">My User</Translate>
          </dt>
          <dd>{myUserMyTestEntity.myUser ? myUserMyTestEntity.myUser.id : ''}</dd>
          <dt>
            <Translate contentKey="jhipsterCourseApp.myUserMyTest.myTest">My Test</Translate>
          </dt>
          <dd>{myUserMyTestEntity.myTest ? myUserMyTestEntity.myTest.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/my-user-my-test" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/my-user-my-test/${myUserMyTestEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default MyUserMyTestDetail;
