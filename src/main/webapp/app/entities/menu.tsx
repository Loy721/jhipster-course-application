import React from 'react';
import { Translate } from 'react-jhipster';

import MenuItem from 'app/shared/layout/menus/menu-item';

const EntitiesMenu = () => {
  return (
    <>
      {/* prettier-ignore */}
      <MenuItem icon="asterisk" to="/file">
        <Translate contentKey="global.menu.entities.file" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/my-test">
        <Translate contentKey="global.menu.entities.myTest" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/question">
        <Translate contentKey="global.menu.entities.question" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/my-user-my-test">
        <Translate contentKey="global.menu.entities.myUserMyTest" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/my-user">
        <Translate contentKey="global.menu.entities.myUser" />
      </MenuItem>
      {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
    </>
  );
};

export default EntitiesMenu;
