import file from 'app/entities/file/file.reducer';
import myTest from 'app/entities/my-test/my-test.reducer';
import question from 'app/entities/question/question.reducer';
import myUser from 'app/entities/my-user/my-user.reducer';
import myUserMyTest from 'app/entities/my-user-my-test/my-user-my-test.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  file,
  myTest,
  question,
  myUser,
  myUserMyTest,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;
