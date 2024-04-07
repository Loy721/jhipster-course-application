import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IFile } from 'app/shared/model/file.model';
import { getEntities as getFiles } from 'app/entities/file/file.reducer';
import { IMyTest } from 'app/shared/model/my-test.model';
import { getEntity, updateEntity, createEntity, reset } from './my-test.reducer';

export const MyTestUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const files = useAppSelector(state => state.file.entities);
  const myTestEntity = useAppSelector(state => state.myTest.entity);
  const loading = useAppSelector(state => state.myTest.loading);
  const updating = useAppSelector(state => state.myTest.updating);
  const updateSuccess = useAppSelector(state => state.myTest.updateSuccess);

  const handleClose = () => {
    navigate('/my-test');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getFiles({}));
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

    const entity = {
      ...myTestEntity,
      ...values,
      file: files.find(it => it.id.toString() === values.file?.toString()),
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
          ...myTestEntity,
          file: myTestEntity?.file?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="jhipsterCourseApp.myTest.home.createOrEditLabel" data-cy="MyTestCreateUpdateHeading">
            <Translate contentKey="jhipsterCourseApp.myTest.home.createOrEditLabel">Create or edit a MyTest</Translate>
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
                  id="my-test-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField label={translate('jhipsterCourseApp.myTest.name')} id="my-test-name" name="name" data-cy="name" type="text" />
              <ValidatedField
                label={translate('jhipsterCourseApp.myTest.questions')}
                id="my-test-questions"
                name="questions"
                data-cy="questions"
                type="text"
              />
              <ValidatedField id="my-test-file" name="file" data-cy="file" label={translate('jhipsterCourseApp.myTest.file')} type="select">
                <option value="" key="0" />
                {files
                  ? files.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/my-test" replace color="info">
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

export default MyTestUpdate;
