const nodemailer = require("nodemailer");

export async function sendCustomVerificationEmail(
  email: string,
  verificationLink: string
) {
  // transport
  const mailTransport = nodemailer.createTransport({
    service: "gmail",
    auth: {
      user: process.env.GMAIL_EMAIL,
      pass: process.env.GMAIL_PASSWORD,
    },
  });

  // email options
  const mailOptions: any = {
    from: `${process.env.APP_NAME} <noreply@firebase.com>`,
    to: email,
  };

  mailOptions.subject = `Request of email verification in ${process.env.APP_NAME}!`;
  mailOptions.text = verificationLink;

  // sending the  link
  await mailTransport.sendMail(mailOptions);
  return null;
}
