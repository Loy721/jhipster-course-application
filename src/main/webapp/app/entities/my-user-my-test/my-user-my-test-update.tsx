import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IMyUser } from 'app/shared/model/my-user.model';
import { getEntities as getMyUsers } from 'app/entities/my-user/my-user.reducer';
import { IMyTest } from 'app/shared/model/my-test.model';
import { getEntities as getMyTests } from 'app/entities/my-test/my-test.reducer';
import { IMyUserMyTest } from 'app/shared/model/my-user-my-test.model';
import { getEntity, updateEntity, createEntity, reset } from './my-user-my-test.reducer';

export const MyUserMyTestUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const myUsers = useAppSelector(state => state.myUser.entities);
  const myTests = useAppSelector(state => state.myTest.entities);
  const myUserMyTestEntity = useAppSelector(state => state.myUserMyTest.entity);
  const loading = useAppSelector(state => state.myUserMyTest.loading);
  const updating = useAppSelector(state => state.myUserMyTest.updating);
  const updateSuccess = useAppSelector(state => state.myUserMyTest.updateSuccess);

  const handleClose = () => {
    navigate('/my-user-my-test');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getMyUsers({}));
    dispatch(getMyTests({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  // eslint-disable-next-line complexity
  const saveEntity = values => {
    if (values.id !== undefined && typeof values.id !== 'number') {
      values.id = Number(values.id);
    }
    if (values.grade !== undefined && typeof values.grade !== 'number') {
      values.grade = Number(values.grade);
    }

    const entity = {
      ...myUserMyTestEntity,
      ...values,
      myUser: myUsers.find(it => it.id.toString() === values.myUser?.toString()),
      myTest: myTests.find(it => it.id.toString() === values.myTest?.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...myUserMyTestEntity,
          myUser: myUserMyTestEntity?.myUser?.id,
          myTest: myUserMyTestEntity?.myTest?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="jhipsterCourseApp.myUserMyTest.home.createOrEditLabel" data-cy="MyUserMyTestCreateUpdateHeading">
            <Translate contentKey="jhipsterCourseApp.myUserMyTest.home.createOrEditLabel">Create or edit a MyUserMyTest</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="my-user-my-test-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('jhipsterCourseApp.myUserMyTest.grade')}
                id="my-user-my-test-grade"
                name="grade"
                data-cy="grade"
                type="text"
              />
              <ValidatedField
                id="my-user-my-test-myUser"
                name="myUser"
                data-cy="myUser"
                label={translate('jhipsterCourseApp.myUserMyTest.myUser')}
                type="select"
              >
                <option value="" key="0" />
                {myUsers
                  ? myUsers.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="my-user-my-test-myTest"
                name="myTest"
                data-cy="myTest"
                label={translate('jhipsterCourseApp.myUserMyTest.myTest')}
                type="select"
              >
                <option value="" key="0" />
                {myTests
                  ? myTests.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/my-user-my-test" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default MyUserMyTestUpdate;
