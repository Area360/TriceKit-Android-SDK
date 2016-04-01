# Table of Contents
- [User data](#feature-user-data)
    - [What is user data?](#feature-user-data-what-is-it)
    - [How does it work?](#feature-user-data-how-does-it-work)
    - [How can you use it?](#feature-user-data-how-can-you-use-it)
        - [URL callback](#feature-user-data-url-callback)
        - [Trigger segmentation](#feature-user-data-trigger-segmentation)
    - [Use case example](#feature-user-data-examples)
- [Segmentation rules](#feature-segmentation-rules)
    - [Expression operators](#feature-expression-operators)
    - [Reserved words](#feature-reserved-words)
    - [Variable namespace](#feature-reserved-words)

# <a name="feature-user-data"></a>User data

## <a name="feature-user-data-what-is-it"></a>What is user data?

User data is meta data associated with users. user data can contain any data as
long as it follows the standard JSON format. Key/value pairs, objects and arrays
are supported.

## <a name="feature-user-data-how-does-it-work"></a>How does it work?

When the TriceKit SDK starts, it will initiate a session. Along with that
session you can provide user data.

> This could for example come from you login system.  When a user logs in, you
could initiate the TriceKit session with email, age and gender.

In order to associate ZTA pings to a user, each TriceKit organization specifies
a user primary key. By default the primary key is the device id. This means that if a
user changes their device, they will look like a new user to TriceKit. To solve
this, you are able to change the primary key [see user data REST API
documentation](http://tricekit.com/api/api_doc.html#user-data) to any of the
user data fields you provide. This can be for example your own `user id` from
your customer database. As soon as the primary key has been changed, TriceKit
will associate any subsequent sessions coming with that primary key to the same
user.

By using our [user data REST
API](http://tricekit.com/api/api_doc.html#user-data-single-user-data-post), you
will also be able to update user data for your users as well. User data does not
necessarily need to come from your app!

## <a name="feature-user-data-how-can-you-use-it"></a>How can you use it?

As soon as you start sending user data, you will be able to use that data to
personalize your user's experience!

### <a name="feature-user-data-url-callback"></a>URL callback

By setting up a URL Callback Action on TriceKit CMS (POST only), you will
receive along that POST any information regarding the Venue, Zone and Trigger
that trigger that action as well as all the user data. This allows you to
identify who is in the zone and triggered the action. This is highly valuable to
you as you will be able to personalize your customer experience.

### <a name="feature-user-data-trigger-segmentation"></a>Trigger segmentation

Once user data are available, you will be able to apply segmentation rules to
your Trigger. You can decide if a trigger fires for a particular user based on
user data. Segmentation rules will be available while creating/editing a trigger
on TriceKit CMS.

## <a name="feature-user-data-examples"></a>Use case example

Primary key has been previously set to `user_id` ([see user data REST API documentation](https://tricekit.com/api/api_doc.html#user data-user data-primary-key-put)).

Example of information you get once the user logged into the app and that you
pass to TriceKit:

```
"name": "Anakin",
"age": "32",
"status": "Gold"
"user_id": "UIEWQ781AHX23"
```

If you want to know where your users spend their time in your venue based on
their age, it is now possible. The only thing you have to do is setup a URL
Callback Action with your own end point that you will have previously developed.

Here is an example of the JSON data that you will get along that POST event.

```
{
    "user_data": {
        "name": "Anakin",
        "age": "32",
        "status": "Gold",
        "user_id": "UIEWQ781AHX23"
    },
    "trigger": {
        "id": 439,
        "zones_uid": "321",
        "name": "Cafe trigger",
        "frequency": 420,
        "event": "enter"
    },
    "zone": {
      "id": 312,
      "name": "Cafe"
    },
    "venue_uid": "UWIEOQ32AHDBJ"
}
```

Now let's use segmentation to personalize the experience of your `GOLD` members.
In order to achieve that, you have to create a Trigger with segmentation rule.

Setting up a segmentation rule `status == "GOLD"` will restrict the trigger
from firing unless the user data field `status` exists and has the value
`GOLD`.

# <a name="feature-segmentation-rules"></a>Segmentation rules

Segmentation rules are simple C-like expressions that are evaluated using
information available when the trigger is firing.

## <a name="feature-expression-operators"></a>Expression operators
The supported operators are (in order of precedence):

- Parentheses "(" ... ")".
- Relational "<", "<=", ">=", ">".
- Equality "=" and inequality "!=".
- Logical AND "&".
- Logical XOR "^".
- Logical OR "|".

The boolean operators are short-circuit evaluated.

## <a name="feature-reserved-words"></a>Reserved words

Reserved words are "true", "false", "null" and "undefined", which are usable in
the following contexts:

- "true" and "false" can be compared to binary values:
    - `booleanVar = true` is the same as `booleanVar`.
    - `booleanVar = false` is the same as `booleanVar ^ true`.  Note that
      since there is no unary logical "not" operator, this is the way of testing
      for a boolean value being false.
- "null" is used to compare against an explicit null value in the JSON data.
  For example, if the user data is `{ user_name: null }`, then the expression
  `user_data.user_name = null` is true.  However, the expression
  `user_data.something_else = null` will fail to evaluate, as something\_else
  is not a valid value in the user data.
- "undefined" is used to compare against a non-present value in the JSON data.
  Using the JSON user data from above, `user_data.user_name = undefined` is
  false, and `user_data.something_else = undefined` is true.

The use of "undefined" is important to ensure correct evaluation of expressions
where user data values might not be present.  Consider the case where there are
two boolean values in the user data that may or may not be set.

- The trigger `user_data.A | user_data.B` will fail to evaluate (and thus the
  trigger will not fire) if "A" is not defined, or if "A" is false and "B" is not
  defined.  Note that due to short-circuit evaluation of the boolean operators,
  the expression will evaluate to true regardless of the type (or omission) of
  "B" if the value of "A" is true.
- The trigger `((user_data.A != undefined) & user_data.A) | ((user_data.B != undefined) & user_data.B)`
  will never fail to evaluate (assuming "A" and "B" are either undefined or set
  to a boolean value), and will evaluate to true if either "A" or "B" are
  defined and true.  This is probably what is usually intended.

## <a name="feature-reserved-words"></a>Variable namespace

Variable names are accessed through a hierarchy that contains information about
the trigger, zone, and user:

- "zone" (object):
    - "uuid" (string): The UUID of the zone.
    - "name" (string): The name of the zone.
    - "metadata" (object): Any metadata (set via the CMS) for the zone.
    - "dwellTime" (float): The time (in seconds) that the user has been in the
      zone.
- "trigger" (object):
    - "uuid" (string): The UUID of the zonetrigger.
    - "name" (string): The name of the trigger.
    - "frequency" (integer) (integer): The minimum time (in milliseconds)
      between trigger fires.
    - "limitType" (string): "none" if there is no limit on the number of times
      the trigger may fire. "per person" if there is a per-person limit.
    - "limit" (integer): The limit for the above limit type.  Undefined if
      "limitType" is "none".
    - "eventType" (string): The type of trigger.  May be "enter", "exit", or
      "dwell".
    - "dwellTime" (integer): The delay between trigger fires for a "dwell"
      trigger.  Undefined if "eventType" is not "dwell".
    - "fireCount" (integer): The number of times that the trigger has fired
      since TriceKit was started.
    - "lastFireTime" (float): The time (in seconds) since the last time that the
      trigger fired.
    - "metadata" (object): Any metadata (set via the CMS) for the trigger.
- "device" (object): Varies depending on platform, but typically will have some
  or all of the following values corresponding to information about the users'
  device:
    - "serial" (string): A per-installation per-device unique ID.
    - "model" (string): The model name.
    - "os" (string): The OS (eg: "iOS" or "Android").
    - "os\_version" (string): The version of the OS.
    - "resolution" (string): The screen resolution in "wxh" format (eg:
      "640x1136").
    - "bluetooth\_enabled" (boolean): Whether bluetooth is enabled.
    - "push\_enabled" (boolean): Whether push notifications ares enabled.
    - "wifi" (string): The SSID of the connected Wi-Fi network.
    - "carrier" (string): The carrier name.
    - "opengl\_version" (string): The OpenGL version.
- "user\_data" (object): User data set on the device or received from the
  server.