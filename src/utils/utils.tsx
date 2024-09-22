export const emailValidator = (email: string) => {
  const re = /\S+@\S+\.\S+/;

  if (!email || email.length <= 0) return 'Παρακαλώ εισάγετε το username σας';
  if (!re.test(email)) return 'Ooops! We need a valid email address.';

  return '';
};

export const usernameValidator = (username: string) => {
  if (!username || username.length <= 0) return 'Παρακαλώ εισάγετε το username σας';

  return '';
};

export const passwordValidator = (password: string) => {
  if (!password || password.length <= 0) return 'Παρακαλώ εισάγετε τον κωδικό σας';

  return '';
};

export const nameValidator = (name: string) => {
  if (!name || name.length <= 0) return 'Name cannot be empty.';

  return '';
};
