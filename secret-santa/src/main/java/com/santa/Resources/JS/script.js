function displayText(id) {
    var text = document.getElementById(id + "c");
    var revealbutton = document.getElementById(id + "a");
    var hidetext = document.getElementById(id + "b");
    text.style.display = "inline-block";
    revealbutton.style.display = "none";
    hidetext.style.display = "inline-block";
  }
  function hideText(id) {
    var text = document.getElementById(id + "c");
    var revealbutton = document.getElementById(id + "a");
    var hidetext = document.getElementById(id + "b");
    text.style.display = "none";
    revealbutton.style.display = "inline-block";
    hidetext.style.display = "none";
  }

  function hideLine(id) {
    document.getElementById(id).remove();
  }

  function deleteEvent(Eventid, GiftId) {
    if(confirm("Are you sure you want to delete this gift?")) {
      console.log("delete event: " + GiftId)
      // URL of the resource to be deleted
      const url = '/report';

      // Configuration object for the fetch request
      const options = {
        method: 'DELETE',
        headers: {
          'Content-Type': 'application/json', // Specify the content type if necessary
          'EventId': Eventid,
          'GiftId': GiftId,
          // Additional headers can be included as needed, such as authentication tokens
        },
      };

      // Send the DELETE request using fetch
      fetch(url, options)
        .then(response => {
          if (!response.ok) {
            alert("Failed to delete");
            throw new Error('Network response was not ok');
          }
          console.log('Resource deleted successfully');
          hideLine(GiftId);
        })
        .catch(error => {
          console.error('There was a problem with the DELETE request:', error.message);
        });
    }
  }