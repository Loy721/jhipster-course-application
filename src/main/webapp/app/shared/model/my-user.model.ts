export interface IMyUser {
  id?: number;
  name?: string | null;
  surname?: string | null;
}

export const defaultValue: Readonly<IMyUser> = {};
