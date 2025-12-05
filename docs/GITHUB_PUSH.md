# Git & GitHub Push Instructions

## Current Repository Status

✅ **Local Repository**: Initialized and committed
- Location: `d:\Library Management System\Library_Management_System`
- Branch: `master`
- Initial commit: Ready

## Step 1: Create a New Repository on GitHub

1. Go to [GitHub](https://github.com)
2. Click "+" icon → "New repository"
3. Fill in the details:
   - **Repository name**: `library-management-system`
   - **Description**: "A comprehensive Java-based Library Management System with Swing GUI and MySQL backend"
   - **Visibility**: Public (or Private)
   - **Initialize repository**: Leave unchecked (we have local files)
4. Click "Create repository"

## Step 2: Add Remote Repository

After creating the repository on GitHub, you'll see instructions. Run this command:

```bash
cd "d:\Library Management System\Library_Management_System"

git remote add origin https://github.com/yourusername/library-management-system.git
```

**Replace `yourusername` with your actual GitHub username**

## Step 3: Rename Branch (Optional)

If GitHub shows `main` as default, rename your local branch:

```bash
git branch -M main
```

Or keep it as `master` and push accordingly.

## Step 4: Push to GitHub

```bash
git push -u origin master
```

Or if using main:
```bash
git push -u origin main
```

This will prompt you to authenticate. You can use:
- **GitHub Personal Access Token** (recommended)
- **GitHub CLI** (gh auth login)

## Authentication Methods

### Option 1: Personal Access Token (Recommended)

1. Go to GitHub → Settings → Developer settings → Personal access tokens
2. Generate new token with `repo` scope
3. Copy the token
4. When prompted for password during `git push`, paste the token

### Option 2: GitHub CLI

```bash
gh auth login
# Follow prompts
git push -u origin master
```

### Option 3: SSH Key

1. Generate SSH key: `ssh-keygen -t ed25519 -C "your-email@example.com"`
2. Add to GitHub: Settings → SSH and GPG keys
3. Test: `ssh -T git@github.com`
4. Use SSH URL: `git remote add origin git@github.com:yourusername/library-management-system.git`

## Step 5: Verify Push

After pushing, verify on GitHub:
```bash
git remote -v
git branch -a
```

Visit: `https://github.com/yourusername/library-management-system`

## Complete Push Command Sequence

```bash
cd "d:\Library Management System\Library_Management_System"

# Add remote
git remote add origin https://github.com/yourusername/library-management-system.git

# Verify remote
git remote -v

# Push to GitHub
git push -u origin master

# Verify
git branch -a
git log --oneline
```

## After First Push

For future pushes, simply use:
```bash
git push
```

## Troubleshooting

### Issue: "fatal: remote origin already exists"
```bash
git remote remove origin
git remote add origin https://github.com/yourusername/library-management-system.git
```

### Issue: "Authentication failed"
- Verify GitHub credentials
- Use Personal Access Token instead of password
- Or use SSH with SSH keys

### Issue: "Repository not found"
- Check repository URL
- Verify GitHub username is correct
- Ensure repository is created on GitHub

## Repository URL Format

- HTTPS: `https://github.com/yourusername/library-management-system.git`
- SSH: `git@github.com:yourusername/library-management-system.git`

## GitHub Repository Structure

After successful push, your repository will contain:

```
📦 library-management-system/
├── 📄 README.md
├── 📄 LICENSE
├── 📄 .gitignore
├── 📁 docs/
│   ├── INSTALLATION.md
│   ├── DATABASE.md
│   └── CONTRIBUTING.md
├── 📁 src/
│   ├── main/java/com/librarymgmt/
│   │   ├── config/
│   │   ├── ui/admin/
│   │   ├── ui/user/
│   │   ├── ui/auth/
│   │   └── utils/
│   └── main/resources/db/
└── 📁 .github/ (for future use)
```

## Next Steps After Push

1. ✅ Repository is public and discoverable
2. Share the link: `https://github.com/yourusername/library-management-system`
3. Add topics: `java`, `swing`, `mysql`, `gui`, `library-management`
4. Enable GitHub Pages (optional)
5. Add project descriptions and tags
6. Invite collaborators (if needed)

## Setting Up GitHub Pages (Optional)

1. Go to Settings → Pages
2. Select source: `main` or `master`
3. Select folder: `/ (root)`
4. Save
5. Site will be available at: `https://yourusername.github.io/library-management-system`

## Maintenance

### Regular Commits
```bash
git add .
git commit -m "feat: description"
git push
```

### Creating Branches for Features
```bash
git checkout -b feature/new-feature
# Make changes
git add .
git commit -m "feat: add new feature"
git push -u origin feature/new-feature
# Create Pull Request on GitHub
```

---

**Ready to push? Run the commands in Step 5!**
