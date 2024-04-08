import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './my-test.reducer';

export const MyTestDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const myTestEntity = useAppSelector(state => state.myTest.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="myTestDetailsHeading">
          <Translate contentKey="jhipsterCourseApp.myTest.detail.title">MyTest</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{myTestEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="jhipsterCourseApp.myTest.name">Name</Translate>
            </span>
          </dt>
          <dd>{myTestEntity.name}</dd>
          <dt>
            <Translate contentKey="jhipsterCourseApp.myTest.file">File</Translate>
          </dt>
          <dd>{myTestEntity.file ? myTestEntity.file.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/my-test" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/my-test/${myTestEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default MyTestDetail;
