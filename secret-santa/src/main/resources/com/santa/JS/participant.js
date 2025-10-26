function submitForm() {
	const email = document.getElementById("email").value;
	const eventId = document.getElementById("EventID").value;
	const password = document.getElementById("password").value;
	const name = document.getElementById("name")?.value;

	if (!email || !eventId) {
		alert('Required form fields are missing');
		return;
	}

	const submitBtn = document.querySelector('button[type="button"]');
	if (submitBtn) submitBtn.disabled = true;

	// Check if the email is already taken for this event.
	fetch('/user/status', {
		method: 'GET',
		headers: {
			'id': eventId,
			'email': email
		},
		credentials: 'include' // Ensure cookies are included in the request
	})
	.then(response => {
		if (response.status === 409) {
			// Email is taken, attempt login
			return fetch('/user/login', {
				method: 'GET',
				headers: {
					'id': eventId,
					'email': email,
					'password': password
				},
				credentials: 'include' // Ensure cookies are included in the request
			});
		} else if (response.ok) {
			if (document.getElementById("register").style.display == "block") {
			// Email is available, attempt registration
					return fetch('/user/register', {
					method: 'GET',
					headers: {
						'id': eventId,
						'email': email,
						'password': password,
						'name': name
					},
					credentials: 'include' // Ensure cookies are included in the request
				});
			}
			else{
				document.getElementById("register").style.display = "block"
				return response = response.ok
			}
		} else {
			throw new Error('Unable to verify email availability.');
		}
	})
	.then(response => {
		if (response.redirected) {
			// Follow the redirect if provided
			const redirectUrl = response.url;
			if (redirectUrl) {
				window.location.href = redirectUrl;
			} else {
				alert('Redirect URL not provided.');
			}
		}
	})
	.catch(err => {
		console.error('Error during form submission:', err);
		alert('An error occurred. Please try again.');
	})
	.finally(() => {
		if (submitBtn) submitBtn.disabled = false;
	});
}