export interface IQuestion {
  id?: number;
  content?: string | null;
}

export const defaultValue: Readonly<IQuestion> = {};
