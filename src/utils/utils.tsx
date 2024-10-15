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
}

//Function that transforms the Scanned MRZ data to an object
export const parseMRZ = (mrzData: String) => {
  // Calculate the midpoint
  const midIndex = Math.floor(mrzData.length / 2);
  // Split the string into two halves
  const mrzLine1 = mrzData.substring(0, midIndex); // or str.slice(0, midIndex)
  const mrzLine2 = mrzData.substring(midIndex);


  // Line 1 parsing
  const documentType = mrzLine1.substring(0, 1); // P
  const issuingCountry = mrzLine1.substring(2, 5); // ESP
  const names = mrzLine1.substring(5).split('<<');
  const surname = names[0].replace(/<+/g, ' ').trim(); // Replace '<' with spaces and trim
  const givenNames = names[1].replace(/<+/g, ' ').trim(); // Replace '<' with spaces and trim

  // Line 2 parsing
  const documentNumber = mrzLine2.substring(0, 9).replace(/<+/g, '').trim(); // Passport number
  const dateOfBirth = mrzLine2.substring(13, 19); //
  const dateOfBirthFormatted = formatDateString(dateOfBirth, true); // Convert to readable date format
  const dateOfExpiry = mrzLine2.substring(21, 27); //
  const dateOfExpiryFormatted = formatDateString(dateOfExpiry, false); // Convert to readable date format
  const nationality = mrzLine2.substring(10, 13); // ESP
  const gender = mrzLine2.substring(20, 21); // M/F

  // Return the parsed fields as an object
  return {
    documentType,
    documentNumber,
    dateOfBirth: dateOfBirthFormatted,
    dateOfExpiry: dateOfExpiryFormatted,
    issuingCountry,
    surname,
    givenNames,
    nationality,
    gender
  };
};

  // Helper function to format date from YYMMDD to a readable format (YYYY-MM-DD)
  export const formatDateString = (dateString: String, smartYear: boolean) => {
    const year = parseInt(dateString.substring(0, 2), 10);
    const month = dateString.substring(2, 4);
    const day = dateString.substring(4, 6);

    // Assume 1900s for years greater than 30, otherwise 2000s
    let fullYear = `20${year}`;
    if (smartYear) {
      fullYear = year > 30 ? `19${year}` : `20${year}`;
    }
    return `${day}/${month}/${fullYear}`;
  };

