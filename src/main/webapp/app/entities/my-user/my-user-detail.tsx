import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './my-user.reducer';

export const MyUserDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const myUserEntity = useAppSelector(state => state.myUser.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="myUserDetailsHeading">
          <Translate contentKey="jhipsterCourseApp.myUser.detail.title">MyUser</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{myUserEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="jhipsterCourseApp.myUser.name">Name</Translate>
            </span>
          </dt>
          <dd>{myUserEntity.name}</dd>
          <dt>
            <span id="surname">
              <Translate contentKey="jhipsterCourseApp.myUser.surname">Surname</Translate>
            </span>
          </dt>
          <dd>{myUserEntity.surname}</dd>
        </dl>
        <Button tag={Link} to="/my-user" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/my-user/${myUserEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default MyUserDetail;
