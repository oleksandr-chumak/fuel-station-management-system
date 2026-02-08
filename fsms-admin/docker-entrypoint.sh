#!/bin/sh

# Replace environment variables in the app.config.json file
cat <<EOF > /usr/share/nginx/html/assets/app.config.json
{
  "apiUrl": "${API_URL}",
  "managerUrl": "${MANAGER_URL}"
}
EOF

# Start Nginx
exec nginx -g 'daemon off;'
